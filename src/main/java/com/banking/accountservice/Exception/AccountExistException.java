package com.banking.accountservice.Exception;

public class AccountExistException extends RuntimeException {

  public AccountExistException(String message) {
    super(message);
  }
}
