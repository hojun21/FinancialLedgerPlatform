package com.example.financialledgersystem.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "accounts")
public class Account extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Column(nullable = false)
    private long balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member owner; // owner is nullable because system account has no owner

    public static final String SYSTEM_CASH = "SYSTEM_CASH";

    // Public constructor for user's account
    public Account(String accountNumber, Member owner) {
        this.accountNumber = accountNumber;
        this.balance = 0L;
        this.owner = Objects.requireNonNull(owner, "customer account needs owner");
    }

    // Private constructor for system's account
    private Account(String accountNumber){
        this.accountNumber = accountNumber;
        this.balance = 0L;
    }

    public static Account systemCash() {
        return new Account(SYSTEM_CASH);
    }

    public boolean isSystem() {
        return owner == null;
    }


}
