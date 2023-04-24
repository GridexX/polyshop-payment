package fr.dopolytech.polyshop.Payment.models;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Shipping {

  public String id;
  public String orderId;
  public OrderStatus status;

  public Shipping() {

  }

  public Shipping(String id, String orderId, OrderStatus status) {
    this.id = id;
    this.orderId = orderId;
    this.status = status;
  }

  public Shipping(Payment payment) {
    this.id = UUID.randomUUID().toString();
    this.orderId = payment.orderId;
    this.status = OrderStatus.delivered;
  }

  @Override
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return "";
    }
  }
}