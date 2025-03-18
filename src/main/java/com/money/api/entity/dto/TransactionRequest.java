package com.money.api.entity.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private String description;
    private Double amount;
}
