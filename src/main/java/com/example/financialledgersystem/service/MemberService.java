package com.example.financialledgersystem.service;

import com.example.financialledgersystem.domain.Member;
import com.example.financialledgersystem.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // generator for private final
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signup(String username, String rawPassword){

        // username duplicate check
        if(memberRepository.existsByUsername(username)){
            throw new IllegalArgumentException("This username already exists.");
        }

        // Create new member & save
        Member member = new Member(username, passwordEncoder.encode(rawPassword));
        return memberRepository.save(member).getId();
    }

}
