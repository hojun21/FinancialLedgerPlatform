package com.example.financialledgersystem.controller;

import com.example.financialledgersystem.domain.Member;
import com.example.financialledgersystem.dto.SignupRequest;
import com.example.financialledgersystem.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController // includes @Responsebody -> return value becomes body
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")  // @RequestBody automaps and fills up DTO
    public ResponseEntity<Long> signup(@RequestBody SignupRequest signupRequest){
        Long id = memberService.signup(signupRequest.username(), signupRequest.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(id); // ResponseEntity can handle
        // status and body
    }

    // Preventing Duplicates
    @ExceptionHandler({IllegalArgumentException.class, DataIntegrityViolationException.class})
    public ResponseEntity<String> handleDuplicateUsername(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("This username already exists.");
    }

    // Getting my user information
    @GetMapping("/me")
    public String me(@AuthenticationPrincipal UserDetails user){
        return user.getUsername();
    }
}
