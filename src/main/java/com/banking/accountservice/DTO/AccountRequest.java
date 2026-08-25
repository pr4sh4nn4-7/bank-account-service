package com.banking.accountservice.DTO;

import java.math.BigDecimal;

import com.banking.accountservice.Entity.AccountType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AccountRequest {

  @NotBlank(message = "Account holder name is required")
  private String accountHolderName;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Phone is required")
  private String phone;

  @NotNull(message = "Account Type is required")
  private AccountType accountType;

  @NotNull(message = "Initial deposit is required")
  @Positive(message = "Initial deposit must be positive")
  private BigDecimal initialDeposit;

}
