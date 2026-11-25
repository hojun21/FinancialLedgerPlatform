package com.example.financialledgersystem.dto;

import jakarta.validation.constraints.Positive;

public record DepositRequest(
        @Positive long amount) {
}
