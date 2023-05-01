package fr.dopolytech.polyshop.Payment.models;

public class MessageConfirmed {
  public long orderId;
  public String status;

  public MessageConfirmed() {
  }

  public MessageConfirmed(long orderId) {
    this.orderId = orderId;
    this.status = "paid";
  }
}