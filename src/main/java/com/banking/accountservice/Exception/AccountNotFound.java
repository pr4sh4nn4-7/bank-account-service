package com.banking.accountservice.Exception;

public class AccountNotFound extends RuntimeException {

  public AccountNotFound(String message) {
    super(message);
  }

}
