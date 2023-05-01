package fr.dopolytech.polyshop.Payment.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dopolytech.polyshop.Payment.models.ErrorMessage;
import fr.dopolytech.polyshop.Payment.models.MessageConfirmed;
import fr.dopolytech.polyshop.Payment.models.Payment;
import fr.dopolytech.polyshop.Payment.models.Shipping;

@Component
public class PaymentService  {

  private final RabbitTemplate rabbitTemplate;
  private final Queue shippingQueue;
  private final Queue paymentCancelQueue;
  private final Queue inventoryConfirmedQueue;

  //Define the logger
  private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

  @Autowired
  public PaymentService(RabbitTemplate rabbitTemplate, Queue shippingQueue, Queue paymentCancelQueue, Queue inventoryConfirmedQueue) {
    this.rabbitTemplate = rabbitTemplate;
    this.shippingQueue = shippingQueue;
    this.paymentCancelQueue = paymentCancelQueue;
    this.inventoryConfirmedQueue = inventoryConfirmedQueue;
  }

  public void receiveMessage(String messageBody) {
    // String messageBody = new String(message);
    logger.info("Received message from payment queue: " + messageBody);
    try {
      ObjectMapper mapper = new ObjectMapper();
      Payment payment =  mapper.readValue(messageBody, Payment.class);

      // Randomly cancel the payment
      if (Math.random() < 0.33) {
        logger.error("Payment canceled. Message send to payment_cancel: {}", messageBody);
        ErrorMessage errorMessage = new ErrorMessage("error", "Payment canceled", payment.orderId, payment.products);
        String errorMessageString = mapper.writeValueAsString(errorMessage);
        rabbitTemplate.convertAndSend(paymentCancelQueue.getName(), errorMessageString);
        return;
      }

      // Send a confirmation message to the inventory service
      MessageConfirmed messageConfirmed = new MessageConfirmed(payment.orderId);
      String messageConfirmedString = mapper.writeValueAsString(messageConfirmed);
      logger.info("Payment confirmed. Message send to inventory_confirmed: {}", messageConfirmedString);
      rabbitTemplate.convertAndSend(inventoryConfirmedQueue.getName(), messageConfirmedString);

      // Send a message to the shipping service
      Shipping shipping = new Shipping(payment);
      String processedMessage = shipping.toString();
      logger.info("Shipping converted from Payment. Message send to shipping: {}", processedMessage);
      rabbitTemplate.convertAndSend(shippingQueue.getName(), processedMessage);
       
    } catch (Exception e) {
      logger.error(messageBody, e);
    }
    
  }

  public void receiveMessageCancel(String message) {
    // Only transfer the message to the inventory service
    logger.info("Received message from shipping_cancel queue: ");
    logger.info("Message send to payment_cancel: {}", message);
    rabbitTemplate.convertAndSend(paymentCancelQueue.getName(), message);
  }
}