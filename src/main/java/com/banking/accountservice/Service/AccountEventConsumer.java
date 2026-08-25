package com.banking.accountservice.Service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// transaction service publish event that will consume by this service
@Service
@Slf4j
@RequiredArgsConstructor

public class AccountEventConsumer {

  private final AccountService accountService;

  /*
   * consume transaction completed event from kafka
   *
   * @params payload
   * 
   * @param payload
   */
  @KafkaListener(topics = "transaction.completed")
  public void consumeTransactionCompleted(
      @Payload Map<String, Object> payload) {
    try {
      String receiverAcoount = (String) payload.get("receiverAccountNumber");
      BigDecimal amount = new BigDecimal(payload.get("amount").toString());

      log.info("Crediting account {} amount: {}", receiverAcoount, amount);
      accountService.creditBalance(receiverAcoount, amount);

    } catch (Exception e) {
      log.error("Error crediting account : {}", e.getMessage());
    }
  }
  /*
   * consume fraud detected event from kafka
   * block the flagged account
   *
   * 
   * 
   * @param payload
   */

  public void consumeFraudDetected(

      @Payload Map<String, Object> payload) {

    try {
      String accountNumber = (String) payload.get("accountNumber");
      log.info("Fraud detedcted!! - blocking the account: {}", accountNumber);
      accountService.blockAccount(accountNumber);

    } catch (Exception e) {
    }

  }

}
