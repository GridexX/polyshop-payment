package fr.dopolytech.polyshop.Payment.configs;

import org.springframework.amqp.core.Queue;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.dopolytech.polyshop.Payment.services.PaymentService;

@Configuration
public class RabbitMqConfig {

  @Bean
  public Queue paymentQueue() {
    return new Queue("payment", false);
  }

  @Bean
  public Queue shippingQueue() {
    return new Queue("shipping", false);
  }

  @Bean
  public MessageListenerAdapter listenerAdapter(PaymentService paymentService) {
    return new MessageListenerAdapter(paymentService, "receiveMessage");
  }

  @Bean
  public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames("payment");
    container.setMessageListener(listenerAdapter);
    return container;
  }
}