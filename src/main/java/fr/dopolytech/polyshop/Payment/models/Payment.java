package fr.dopolytech.polyshop.Payment.models;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Payment {
  public String id;
  public long orderId;
  public List<ProductItem> products;

  public Payment() {
  }

  public Payment(long orderId, List<ProductItem> products) {
    this.id = UUID.randomUUID().toString();
    this.orderId = orderId;
    this.products = products;
  }

  public Payment(String id, long orderId) {
    this.id = id;
    this.orderId = orderId;
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
