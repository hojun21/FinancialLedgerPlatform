package com.example.financialledgersystem.service;

import com.example.financialledgersystem.domain.Account;
import com.example.financialledgersystem.domain.Member;
import com.example.financialledgersystem.repository.AccountRepository;
import com.example.financialledgersystem.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public String open(String username){
        /*
        @param username : username of Member who opens new account
        @returns accountNumber of new Account
         */

        // Find member
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("Member not found")
        );
        // Create random 12 digits account number
        String accountNumber = String.format("%012d",
                ThreadLocalRandom.current().nextLong(1_000_000_000_000L));

        return accountRepository.save(new Account(accountNumber, member)).getAccountNumber();
    }
}
