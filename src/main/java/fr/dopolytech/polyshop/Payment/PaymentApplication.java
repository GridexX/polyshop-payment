package fr.dopolytech.polyshop.Payment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentApplication {

  private static final Logger logger = LoggerFactory.getLogger(PaymentApplication.class);

  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(PaymentApplication.class, args);

    logger.info("Waiting for messages from inventory queue...");

    // Wait indefinitely for messages to be received
    synchronized (PaymentApplication.class) {
      PaymentApplication.class.wait();
    }
  }
}