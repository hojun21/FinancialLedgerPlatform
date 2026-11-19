package com.example.financialledgersystem.repository;

import com.example.financialledgersystem.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
