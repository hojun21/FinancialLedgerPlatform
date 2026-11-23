package com.example.financialledgersystem.service;

import com.example.financialledgersystem.domain.Account;
import com.example.financialledgersystem.domain.Member;
import com.example.financialledgersystem.dto.AccountResponse;
import com.example.financialledgersystem.repository.AccountRepository;
import com.example.financialledgersystem.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;


    /*
        @param username : username of Member who opens new account
        @returns accountNumber of new Account
    */
    @Transactional
    public String open(String username) {

        // Find member
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("Member not found")
        );
        // Create random 12 digits account number
        String accountNumber = String.format("%012d",
                ThreadLocalRandom.current().nextLong(1_000_000_000_000L));

        return accountRepository.save(new Account(accountNumber, member)).getAccountNumber();
    }

    /*
        @param username
        @return List of AccountResponse Dto belongs to Member with given @username
     */
    @Transactional(readOnly = true)
    public List<AccountResponse> findMine(String username){

        return accountRepository.findAllByOwnerUsername(username).stream()
                .map(AccountResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public Optional<AccountResponse> findMine(String username, String accountNumber){
        return accountRepository.findByAccountNumberAndOwnerUsername(accountNumber, username)
                .map(AccountResponse::from);
    }
}
