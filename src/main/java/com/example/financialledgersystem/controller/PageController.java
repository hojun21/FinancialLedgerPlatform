package com.example.financialledgersystem.controller;

import com.example.financialledgersystem.dto.AccountResponse;
import com.example.financialledgersystem.dto.SignupRequest;
import com.example.financialledgersystem.service.AccountService;
import com.example.financialledgersystem.service.LedgerService;
import com.example.financialledgersystem.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final MemberService memberService;
    private final AccountService accountService;
    private final LedgerService ledgerService;

    @GetMapping("/web/login")
    public String login() { return "login"; }

    @GetMapping("/web/signup")
    public String signup() { return "signup"; }

    @GetMapping("/web/accounts")
    public String accounts(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("accounts", accountService.findMine(user.getUsername()));
        return "accounts";
    }

    @GetMapping("/web/accounts/{accountNumber}")
    public String account(@PathVariable String accountNumber, Model model,
                          @AuthenticationPrincipal UserDetails user) {

        Optional<AccountResponse> account = accountService.findMine(user.getUsername(), accountNumber);
        if(account.isEmpty()){
            return "redirect:/web/accounts";
        }

        // account exists
        model.addAttribute("account", account.get());
        return "account";
    }

    @GetMapping("/web/ledger")
    public String ledger(Model model) {
        model.addAttribute("tb", ledgerService.trialBalance());
        return "ledger";
    }

    @PostMapping("/web/signup")
    public String signup(@ModelAttribute SignupRequest request, Model model){

        try {
            memberService.signup(request.username(), request.password());
        } catch(IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
        // successful sign up
        return "redirect:/web/login?signedUp";
    }

    @PostMapping("/web/accounts")
    public String open(@AuthenticationPrincipal UserDetails user,
                       RedirectAttributes redirectAttributes){

        String accountNumber = accountService.open(user.getUsername());
        redirectAttributes.addFlashAttribute("msg", "Opened " + accountNumber);
        return "redirect:/web/accounts";

    }

    @PostMapping("/web/accounts/{accountNumber}/deposit")
    public String deposit(@AuthenticationPrincipal UserDetails user,
                          @PathVariable String accountNumber,
                          @RequestParam long amount,
                          RedirectAttributes redirectAttributes){
        try {
            ledgerService.deposit(user.getUsername(), accountNumber, amount);
            redirectAttributes.addFlashAttribute("msg", "Deposited " + amount);
        } catch (IllegalArgumentException | IllegalStateException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/accounts/" + accountNumber;
    }

    @PostMapping("/web/accounts/{accountNumber}/transfer")
    public String transfer(@AuthenticationPrincipal UserDetails user,
                           @PathVariable String accountNumber,
                           @RequestParam long amount,
                           @RequestParam String toAccountNumber,
                           RedirectAttributes redirectAttributes){
        try {
            ledgerService.transfer(user.getUsername(), accountNumber, toAccountNumber, amount);
            redirectAttributes.addFlashAttribute("Sent" + amount + "to" + toAccountNumber + "Successfully");
        } catch (IllegalStateException | IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error has occurred", e.getMessage());
        }
        return "redirect:/web/accounts" + accountNumber;
    }

}
