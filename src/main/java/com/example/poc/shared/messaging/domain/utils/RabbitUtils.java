package com.example.poc.shared.messaging.domain.utils;

import static com.example.poc.shared.common.domain.utils.JSON.MAPPER;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.io.IOException;
import java.util.List;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import com.example.poc.shared.messaging.domain.models.ChannelManager;
import com.example.poc.shared.messaging.domain.models.Error;
import com.example.poc.shared.messaging.domain.models.Fail;
import com.example.poc.shared.messaging.domain.models.FailImpl;
import com.example.poc.shared.messaging.domain.models.Message;
import com.example.poc.shared.messaging.domain.models.MessageImpl;
import com.example.poc.shared.messaging.domain.models.RetryListener;

public class RabbitUtils {
  public static RabbitAdmin admin(ConnectionFactory factory) {
    return new RabbitAdmin(factory);
  }

  public static <T> T parse(
    byte[] bodyBytes,
    RetryListener<T> listener
  ) throws IOException {
    try {
      var content = new String(bodyBytes, UTF_8);
      return listener.parse(content);
    } catch (Exception exception) {
//      listener.onMaxRetryAttempts(new Fail<>(exception, null));
//      log.debug("Erro ao fazer o parse. Descartando da mensagem. {}", extractMessage(exception));
      return null;
    }
  }

  public static RabbitTemplate createTemplate(ConnectionFactory connection) {
    final var template = new RabbitTemplate(connection);

    template.setMessageConverter(new Jackson2JsonMessageConverter(MAPPER));
//    template.addBeforePublishPostProcessors(TraceId::addTraceId);

    return template;
  }

  public static <E> Fail<E> fail(Throwable cause, E body) {
    return new FailImpl<>(cause, body);
  }

  public static <E> Fail<E> fail(List<Error> errors, E body) {
    return new FailImpl<>(errors, body);
  }

  public static <E> Message<E> message(E body, ChannelManager manager, MessageProperties properties) {
    return new MessageImpl<>(body, manager, properties);
  }
}
