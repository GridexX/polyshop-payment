package fr.dopolytech.polyshop.Payment.models;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Payment {
  public String id;
  public String orderId;
  public long amount;

  public Payment() {
  }

  public Payment(String orderId, long amount) {
    this.id = UUID.randomUUID().toString();
    this.orderId = orderId;
    this.amount = amount;
  }

  public Payment(String id, String orderId, long amount) {
    this.id = id;
    this.orderId = orderId;
    this.amount = amount;
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
