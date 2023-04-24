package fr.dopolytech.polyshop.Payment.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dopolytech.polyshop.Payment.models.Payment;
import fr.dopolytech.polyshop.Payment.models.Shipping;

@Component
public class PaymentService  {

  private final RabbitTemplate rabbitTemplate;
  private final Queue shippingQueue;

  //Define the logger
  private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

  @Autowired
  public PaymentService(RabbitTemplate rabbitTemplate, Queue shippingQueue) {
    this.rabbitTemplate = rabbitTemplate;
    this.shippingQueue = shippingQueue;
  }

  public void receiveMessage(byte[] message) {
    String messageBody = new String(message);
    logger.info("Received message from inventory queue: " + messageBody);
    try {
      ObjectMapper mapper = new ObjectMapper();
      Payment payment =  mapper.readValue(messageBody, Payment.class);

      Shipping shipping = new Shipping(payment);
      String processedMessage = shipping.toString();
      logger.info("Shipping converted from Payment. Message send to shipping: {}", processedMessage);
      rabbitTemplate.convertAndSend(shippingQueue.getName(), processedMessage);
       
    } catch (Exception e) {
      logger.error(messageBody, e);
    }

    
  }
}