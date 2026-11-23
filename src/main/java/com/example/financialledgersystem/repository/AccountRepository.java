package com.example.financialledgersystem.repository;

import com.example.financialledgersystem.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAllByOwnerUsername(String username);

    Optional<Account> findByAccountNumberAndOwnerUsername(String accountNumber, String username);
}
