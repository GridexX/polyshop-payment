package fr.dopolytech.polyshop.Payment.models;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Payment {
  public String id;
  public String orderId;

  public Payment() {
  }

  public Payment(String orderId) {
    this.id = UUID.randomUUID().toString();
    this.orderId = orderId;
  }

  public Payment(String id, String orderId) {
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
