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

  // Queue used to receive the payment from the inventory service
  @Bean
  public Queue paymentQueue() {
    return new Queue("payment", false);
  }

  // Queue used to cancel the payment to the inventory service
  @Bean
  public Queue paymentCancelQueue() {
    return new Queue("payment_cancel", false);
  }

  // Queue used to confirm the payment to the order service
  @Bean
  public Queue inventoryConfirmedQueue() {
    return new Queue("inventory_confirmed", false);
  }

  @Bean
  public Queue shippingQueue() {
    return new Queue("shipping", false);
  }

  // Queue used for the shipping to cancel the payment
  @Bean
  public Queue shippingCancelQueue() {
    return new Queue("shipping_cancel", false);
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

  @Bean
  public MessageListenerAdapter listenerAdapterCancel(PaymentService paymentService) {
    return new MessageListenerAdapter(paymentService, "receiveMessageCancel");
  }

  @Bean
  public SimpleMessageListenerContainer containerCancel(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapterCancel) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(shippingCancelQueue().getName());
    container.setMessageListener(listenerAdapterCancel);
    return container;
  }
}