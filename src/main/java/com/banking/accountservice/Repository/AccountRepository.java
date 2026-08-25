package com.banking.accountservice.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.accountservice.Entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

  boolean existsByEmail(String email);

  boolean existsByAccountNumber(String accountNumber);

  Optional<Account> findByAccountNumber(String accountNumber);

}
