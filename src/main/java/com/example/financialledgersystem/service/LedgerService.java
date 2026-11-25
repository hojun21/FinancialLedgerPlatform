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
        entry.addLine(system, Direction.DEBIT, amount);
        journalEntryRepository.save(entry);
        return AccountResponse.from(account);
    }

    @Transactional
    public AccountResponse transfer(String fromUsername, String fromNumber, String toNumber, long amount){

        if(amount<=0){
            throw new IllegalArgumentException("Invalid amount");
        }

        // to Account and from Account must be different
        if(fromNumber.equals(toNumber)){
            throw new IllegalArgumentException("Can't transfer to myself");
        }

        // Get From account
        Account from = accountRepository.findByAccountNumberAndOwnerUsername(fromNumber, fromUsername).
                orElseThrow(() -> new IllegalArgumentException("invalid argument"));

        // Get To account (To account cannot be system account)
        Account to = accountRepository.findByAccountNumber(toNumber).filter(a -> !a.isSystem())
                .orElseThrow(() -> new IllegalArgumentException("invalid argument"));

        JournalEntry transfer = new JournalEntry("transfer");
        transfer.addLine(from, Direction.DEBIT, amount);
        transfer.addLine(to, Direction.CREDIT, amount);
        journalEntryRepository.save(transfer);
        return AccountResponse.from(from);
    }

}
