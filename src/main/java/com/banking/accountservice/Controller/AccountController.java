package com.banking.accountservice.Controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.accountservice.DTO.AccountRequest;
import com.banking.accountservice.DTO.AccountResponse;
import com.banking.accountservice.Service.AccountService;

import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/account")

@RequiredArgsConstructor
@Slf4j
public class AccountController {

  private final AccountService accountService;

  @PostMapping("/register")
  public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {

    return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request));

  }

  @GetMapping("/{accountNumber}")
  public ResponseEntity<AccountResponse> getAccount(
      @PathVariable String accountNumber) {
    return ResponseEntity.ok(accountService.getAccount(accountNumber));

  }

  @GetMapping("/{accountNumber}/balance")
  public ResponseEntity<BigDecimal> getBalance(
      @PathVariable String accountNumber) {
    return ResponseEntity.ok(accountService.getBalance(accountNumber));

  }

  @PutMapping("/{accountNumber}/block")

  public ResponseEntity<String> blockAccount(
      @PathVariable String accountNumber) {
    accountService.blockAccount(accountNumber);
    return ResponseEntity.ok("Account blocked");

  }

  @PutMapping("/{accountNumber}/deduct")
  public ResponseEntity<String> deductBalance(
      @PathVariable String accountNumber,
      @RequestParam BigDecimal amount) {
    accountService.deductService(accountNumber, amount);
    return ResponseEntity.accepted().body("Amount deducted successfully");

  }
  /*
   * Compensating transaction endpoint
   * called by TRANSACTION service in two scenarios
   * 1. fraud deducted -> refund sender
   * 2. transaction completed -> credit receiver
   */

  public ResponseEntity<String> creaditBalance(
      @PathVariable String accountNumber,
      @RequestParam BigDecimal amount

  ) {
    accountService.creditBalance(accountNumber, amount);
    return ResponseEntity.ok("Balance credited successfully");

  }

}
