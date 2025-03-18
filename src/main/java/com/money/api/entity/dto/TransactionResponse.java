package com.money.api.entity.dto;

import com.money.api.entity.Transaction;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionResponse {
    private Long id;
    private Long safeId;
    private Long userId;
    private String description;
    private LocalDateTime time;
    private Double amount;

    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.safeId = transaction.getSafe().getId();
        this.userId = transaction.getUserId();
        this.description = transaction.getDescription();
        this.time = transaction.getTime();
        this.amount = transaction.getAmount();
    }
}
