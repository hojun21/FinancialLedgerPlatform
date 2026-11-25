package com.example.financialledgersystem.dto;

public record TrialBalanceResponse(long debit, long credit, long difference) {

    public static TrialBalanceResponse of(long debit, long credit) {
        return new TrialBalanceResponse(debit, credit, debit - credit);
    }
}
