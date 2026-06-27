package com.example.poc.shared.messaging.domain.models.connection;

import static org.springframework.amqp.core.AcknowledgeMode.MANUAL;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import com.example.poc.shared.common.domain.utils.JSON;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListenerSettings {
  private Boolean defaultRequeueRejected;
  private Integer concurrentConsumers;
  private Integer prefetch;

  public SimpleRabbitListenerContainerFactory getManualContainerFactory(ConnectionFactory connection) {
    var listenerContainer = new SimpleRabbitListenerContainerFactory();
    listenerContainer.setMessageConverter(new Jackson2JsonMessageConverter());
    listenerContainer.setConnectionFactory(connection);
    listenerContainer.setDefaultRequeueRejected(this.getDefaultRequeueRejected());
    listenerContainer.setConcurrentConsumers(this.getConcurrentConsumers());
    listenerContainer.setPrefetchCount(this.getPrefetch());
    listenerContainer.setAfterReceivePostProcessors(JSON::setContentTypeJson);
    return listenerContainer;
  }

  public SimpleMessageListenerContainer getCustomManualContainer(ConnectionFactory connectionFactory) {
    var container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setDefaultRequeueRejected(this.getDefaultRequeueRejected());
    container.setAcknowledgeMode(MANUAL);
    container.setConcurrentConsumers(this.getConcurrentConsumers());
    container.setPrefetchCount(this.getPrefetch());
    return container;
  }
}