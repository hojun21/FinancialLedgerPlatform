package com.example.financialledgersystem.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String description;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.PERSIST)
    private List<LedgerLine> lines = new ArrayList<>();

    public JournalEntry(String description) {
        this.description = description;
    }

    public void addLine(Account account, Direction direction, long amount){
        lines.add(new LedgerLine(this, account, direction, amount));
        account.apply(direction, amount);
    }
}
