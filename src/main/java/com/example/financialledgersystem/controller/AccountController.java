package com.example.financialledgersystem.controller;

import com.example.financialledgersystem.domain.LedgerLine;
import com.example.financialledgersystem.dto.AccountResponse;
import com.example.financialledgersystem.dto.DepositRequest;
import com.example.financialledgersystem.service.AccountService;
import com.example.financialledgersystem.service.LedgerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final LedgerService ledgerService;

    /*
    Open new account using given user information
     */
    @PostMapping("/accounts")
    public ResponseEntity<String> open(@AuthenticationPrincipal UserDetails user){

        String accountNumber = accountService.open(user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(accountNumber);
    }

    /*
    @return list of AccountResponse dto with given UserDetails
     */
    @GetMapping("/accounts")
    public List<AccountResponse> findAllAccounts(@AuthenticationPrincipal UserDetails user){
        return accountService.findMine(user.getUsername());
    }

    /*
        @returns
        200 + account info if account is found
        404 if account is not found
     */
    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<AccountResponse> findAccount(@AuthenticationPrincipal UserDetails user,
                                                       @PathVariable String accountNumber){
        return ResponseEntity.of(accountService.findMine(user.getUsername(), accountNumber));
    }

    @PostMapping("/accounts/{accountNumber}/deposit")
    public AccountResponse deposit(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String accountNumber,
            @RequestBody DepositRequest request
            ){

        return ledgerService.deposit(user.getUsername(), accountNumber, request.amount());
    }
}
