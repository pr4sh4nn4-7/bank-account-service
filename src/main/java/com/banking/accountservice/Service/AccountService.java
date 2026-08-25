package com.banking.accountservice.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.banking.accountservice.DTO.AccountRequest;
import com.banking.accountservice.DTO.AccountResponse;
import com.banking.accountservice.Entity.Account;
import com.banking.accountservice.Entity.AccountStatus;
import com.banking.accountservice.Entity.AccountType;
import com.banking.accountservice.Exception.AccountExistException;
import com.banking.accountservice.Exception.AccountNotFound;
import com.banking.accountservice.Exception.InsufficientBalanceException;
import com.banking.accountservice.Repository.AccountRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
  private final AccountRepository accountRepository;
  private final SecureRandom secureRandom = new SecureRandom();

  public AccountResponse createAccount(AccountRequest request) {
    log.info("creating account for: {}", request.getEmail());
    if (accountRepository.existsByEmail(request.getEmail())) {
      throw new AccountExistException("Account already exists for email:" + request.getEmail());

    }
    Account account = new Account();
    account.setAccountHolderName(request.getAccountHolderName());
    account.setEmail(request.getEmail());
    account.setAccountType(request.getAccountType());
    account.setBalance(request.getInitialDeposit());
    account.setPhone(request.getPhone());
    account.setStatus(AccountStatus.ACTIVE);
    String accountNumber = generateAccountNumber();
    account.setAccountNumber(accountNumber);
    account.setDailyTransactionLimit(
        request.getAccountType() == AccountType.SAVING
            ? new BigDecimal(100000)
            : new BigDecimal(500000));

    Account savedAccount = accountRepository.save(account);
    return mapToAccountResponse(account);
  }

  public AccountResponse getAccount(String accountNumber) {
    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new AccountNotFound("Account not found"));
    return mapToAccountResponse(account);
  }

  public BigDecimal getBalance(String accountNumber) {

    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new AccountNotFound("Account not found"));
    return account.getBalance();
  }

  public void blockAccount(String accountNumber) {

    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new AccountNotFound("Account not found"));
    account.setStatus(AccountStatus.BLOCKED);
    accountRepository.save(account);
    log.info("Account blocked: {}", accountNumber);
  }

  public void deductService(String accountNumber, BigDecimal amount) {
    log.info("deducting balance {} from account: {}", amount, accountNumber);

    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new AccountNotFound("Account not found"));

    if (account.getStatus() != AccountStatus.ACTIVE) {
      throw new RuntimeException("Account not active");

    }
    if (account.getBalance().compareTo(amount) < 0) {
      throw new InsufficientBalanceException("Insufficient fund for account");

    }
    account.setBalance(account.getBalance().subtract(amount));
    accountRepository.save(account);
    log.info("balance updated to: {}", account.getBalance());

  }

  private String generateAccountNumber() {
    String accountNumber;
    do {
      long number = secureRandom.nextLong(1_000_000_000_000L);
      accountNumber = String.format("%012d", number);

    } while (accountRepository.existsByAccountNumber(accountNumber));
    return accountNumber;

  }

  private AccountResponse mapToAccountResponse(Account account) {
    AccountResponse accRes = new AccountResponse();
    accRes.setId(account.getId());
    accRes.setAccountNumber(account.getAccountHolderName());
    accRes.setAccountNumber(account.getAccountNumber());
    accRes.setAccountType(account.getAccountType());
    accRes.setEmail(account.getEmail());
    accRes.setPhone(account.getPhone());
    accRes.setStatus(account.getStatus());
    accRes.setBalance(account.getBalance());
    accRes.setDailyTransactionLimit(account.getDailyTransactionLimit());
    accRes.setCreatedAt(LocalDateTime.now());
    return accRes;
  }

  public void creditBalance(String accountNumber, BigDecimal amount) {
    log.info("crediting {} to account: {}", amount, accountNumber);

    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new AccountNotFound("Account not found"));

    account.setBalance(account.getBalance().add(amount));
    accountRepository.save(account);
    log.info("Balance credited: New balance : {}", account.getBalance());

  }

}
