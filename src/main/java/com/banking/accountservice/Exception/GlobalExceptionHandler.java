package com.banking.accountservice.Exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({
      MethodArgumentNotValidException.class,
      AccountNotFound.class
  })
  public ResponseEntity<Map<String, Object>> handleException(Exception ex) {

    Map<String, Object> errors = new HashMap<>();

    errors.put("success", false);

    if (ex instanceof MethodArgumentNotValidException validationException) {

      String message = validationException.getBindingResult()
          .getFieldErrors()
          .stream()
          .map(error -> error.getDefaultMessage())
          .collect(Collectors.joining(", "));

      errors.put("message", message);
      errors.put("status", HttpStatus.BAD_REQUEST.value());

      return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    if (ex instanceof AccountNotFound) {

      errors.put("message", ex.getMessage());
      errors.put("status", HttpStatus.NOT_FOUND.value());
      errors.put("timestamp", LocalDateTime.now());

      return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({ InsufficientBalanceException.class })
  public ResponseEntity<Map<String, Object>> InsufficientBalanceValidation(
      InsufficientBalanceException ex) {

    Map<String, Object> error = new HashMap<>();

    error.put("success", false);
    error.put("message", ex.getMessage());
    error.put("status", HttpStatus.BAD_REQUEST.value());
    error.put("timestamp", LocalDateTime.now());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ AccountExistException.class })
  public ResponseEntity<Map<String, Object>> AccountExistVAlidation(
      AccountExistException ex) {

    Map<String, Object> error = new HashMap<>();

    error.put("success", false);
    error.put("message", ex.getMessage());
    error.put("status", HttpStatus.BAD_REQUEST.value());
    error.put("timestamp", LocalDateTime.now());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

}
