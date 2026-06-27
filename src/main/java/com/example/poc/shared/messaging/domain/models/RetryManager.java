package com.example.poc.shared.messaging.domain.models;

import static com.example.poc.shared.common.domain.utils.ExceptionUtils.extractMessage;
import static com.example.poc.shared.common.domain.utils.TimeUtils.elapsedTime;
import static com.example.poc.shared.messaging.domain.models.ChannelManager.create;
import static com.example.poc.shared.messaging.domain.utils.RabbitUtils.fail;
import static com.example.poc.shared.messaging.domain.utils.RabbitUtils.message;
import static com.example.poc.shared.messaging.domain.utils.RabbitUtils.parse;
import static java.time.Instant.now;
import static java.util.Objects.isNull;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import com.example.poc.shared.exceptions.domain.DisposableMessageException;
import com.example.poc.shared.exceptions.domain.NotRetryableFailureException;
import com.rabbitmq.client.Channel;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class RetryManager<T> implements ChannelAwareMessageListener {
  private final Queue queue;
  private final RetryListener<T> listener;
  private final Validator validator;
  
  @Override
  public void onMessage(Message content, Channel channel) throws Exception {
    var manager = create(
      channel,
      content.getMessageProperties(),
      queue.maxRetryAttempts()
    );

    if (!queue.enabled()) {
      manager.doAck();
      return;
    }

    var body = parse(content.getBody(), listener);

    if (isNull(body)) {
      manager.doAck();
      return;
    }

    if (manager.getCurrentAttempt() <= 1) {
      listener.preValidation(body);
    }

    var violations = validator.validate(body);

    if (!violations.isEmpty()) {
      
      manager.doAck();
      var errors = violations
        .stream()
        .map(Error::new)
        .toList();
      
      listener.onValidationError(fail(errors, body));
      
      log.debug("Erro ao fazer o parse. Payload invalido.");
      return;
    }

    var message = message(
      body,
      manager,
      content.getMessageProperties()
    );

    try {
      log.debug("queue: {}, processamento de mensagem iniciado", queue.alias());

      var start = now();

      if (listener.canAccept(message)) {
        listener.onMessage(message);
      }

      manager.doAck();

      var end = now();

      log.debug(
        "queue: {}, processamento de mensagem finalizado em {}",
        queue.alias(),
        elapsedTime(start, end)
      );

    } catch (NotRetryableFailureException exception) {

      log.error(
        "queue: {}, falha não re-tentável, erro: {}",
        queue.alias(),
        extractMessage(exception)
      );

      manager.doAck();
      listener.onMaxRetryAttempts(fail(exception, body));

    } catch (DisposableMessageException exception) {

      manager.doAck();

      log.debug(
        "queue: {}, descartando mensagem, motivo: {}",
        queue.alias(),
        extractMessage(exception)
      );

      listener.onDisposeMessage(exception, message);
    } catch (Exception exception) {

      if (manager.alreadyReachedMaxOfAttempts()) {

        log.error(
          "queue: {}, limite de tentativas excedido, tentativa: {}, erro: {}",
          queue.alias(),
          manager.getCurrentAttempt(),
          extractMessage(exception)
        );

        manager.doAck();
        listener.onMaxRetryAttempts(fail(exception, body));
      } else {

        manager.doReject();

        log.error(
          "queue:{}, erro durante processamento de mensagem, tentativa: {}, erro {}",
          queue.alias(),
          manager.getCurrentAttempt(),
          extractMessage(exception)
        );
      }
    } finally {
      if (manager.isRunning()) {
        manager.doAck();
      }
    }
  }
}
