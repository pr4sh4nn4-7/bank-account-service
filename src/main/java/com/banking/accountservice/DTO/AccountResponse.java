package com.banking.accountservice.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.banking.accountservice.Entity.AccountStatus;
import com.banking.accountservice.Entity.AccountType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {

  private String id;
  private String accountNumber;
  private String accountHolderName;
  private String email;
  private String phone;
  private AccountType accountType;
  private AccountStatus status;
  private BigDecimal balance;
  private BigDecimal dailyTransactionLimit;
  private LocalDateTime createdAt;

}
