package com.example.financialledgersystem.dto;

import com.example.financialledgersystem.domain.Account;

import java.time.Instant;

/*
Dto object for class Account
 */
public record AccountResponse(String accountNumber, long balance, Instant createdAt) {

    public static AccountResponse from(Account account){
        return new AccountResponse(account.getAccountNumber(), account.getBalance(),
                account.getCreatedAt());
    }
}
