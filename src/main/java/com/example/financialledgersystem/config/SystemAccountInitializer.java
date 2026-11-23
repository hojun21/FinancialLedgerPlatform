package com.example.financialledgersystem.config;

import com.example.financialledgersystem.domain.Account;
import com.example.financialledgersystem.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/*
Generates system account when the application boots
 */
@Component
@RequiredArgsConstructor
public class SystemAccountInitializer implements ApplicationRunner {

    private final AccountRepository accountRepository;

    /*
    Generates System Account if not exists
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(accountRepository.findByAccountNumber(Account.SYSTEM_CASH).isEmpty()){
            accountRepository.save(Account.systemCash());
        }
    }
}
