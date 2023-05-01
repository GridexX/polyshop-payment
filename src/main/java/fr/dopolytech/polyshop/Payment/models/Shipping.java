package fr.dopolytech.polyshop.Payment.models;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Shipping {

  public String id;
  public long orderId;
  public OrderStatus status;
  public List<ProductItem> products;

  public Shipping() {

  }

  public Shipping(String id, long orderId, OrderStatus status) {
    this.id = id;
    this.orderId = orderId;
    this.status = status;
  }

  public Shipping(Payment payment) {
    this.id = UUID.randomUUID().toString();
    this.orderId = payment.orderId;
    this.status = OrderStatus.delivered;
    this.products = payment.products;
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