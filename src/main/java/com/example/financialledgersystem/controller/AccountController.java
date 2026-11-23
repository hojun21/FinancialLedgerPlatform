package com.example.financialledgersystem.controller;

import com.example.financialledgersystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /*
    Open new account using given user information
     */
    @PostMapping("/accounts")
    public ResponseEntity<String> open(@AuthenticationPrincipal UserDetails user){

        String accountNumber = accountService.open(user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(accountNumber);
    }

}
