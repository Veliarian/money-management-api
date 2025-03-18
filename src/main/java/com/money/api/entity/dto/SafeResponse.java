package com.money.api.entity.dto;

import com.money.api.entity.Safe;
import com.money.api.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class SafeResponse {
    private Long id;
    private String name;
    private Double amount;
    private List<String> members;

    public SafeResponse(Safe safe) {
        this.id = safe.getId();
        this.name = safe.getName();
        this.amount = safe.getAmount();
        this.members = safe.getUsers().stream()
                .map(User::getUsername).toList();
    }
}
