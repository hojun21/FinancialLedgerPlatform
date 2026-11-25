package com.example.financialledgersystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record TransferRequest(
        @NotBlank String toAccountNumber,
        @Positive long amount) {
}
