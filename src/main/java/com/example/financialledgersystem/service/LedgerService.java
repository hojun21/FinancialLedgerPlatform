package com.example.financialledgersystem.service;

import com.example.financialledgersystem.domain.Account;
import com.example.financialledgersystem.domain.Direction;
import com.example.financialledgersystem.domain.JournalEntry;
import com.example.financialledgersystem.dto.AccountResponse;
import com.example.financialledgersystem.repository.AccountRepository;
import com.example.financialledgersystem.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final JournalEntryRepository journalEntryRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public AccountResponse deposit(String username, String accountNumber, long amount){

        if(amount <= 0){
            throw new IllegalArgumentException("Invalid amount");
        }

        // deposit
        Account account = accountRepository.findByAccountNumberAndOwnerUsername(accountNumber, username)
                .orElseThrow(() -> new IllegalArgumentException("invalid argument"));
        Account system = accountRepository.findByAccountNumber(Account.SYSTEM_CASH)
                .orElseThrow(() -> new IllegalArgumentException("invalid argument"));

        // Initialize new JournalEntry & LedgerLine
        JournalEntry entry = new JournalEntry("deposit");
        entry.addLine(account, Direction.CREDIT, amount);
        entry.addLine(account, Direction.DEBIT, amount);
        journalEntryRepository.save(entry);
        return AccountResponse.from(account);
    }

}
