package com.example.financialledgersystem.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ledger_lines")
public class LedgerLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="journal_entry_id", nullable=false, updatable=false)
    private JournalEntry journalEntry;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, length=6)
    private Direction direction;

    @Column(nullable = false, updatable = false)
    private long amount;

    // package private
    LedgerLine(JournalEntry journalEntry, Account account, Direction direction, long amount) {
        this.journalEntry = journalEntry;
        this.account = account;
        this.direction = direction;
        this.amount = amount;
    }
}
