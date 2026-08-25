package com.banking.accountservice.Exception;

import java.lang.reflect.Constructor;

public class InsufficientBalanceException extends RuntimeException {

  public InsufficientBalanceException(String message) {
    super(message);
  }

}
