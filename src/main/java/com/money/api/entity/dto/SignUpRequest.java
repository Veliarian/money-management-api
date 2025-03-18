package com.money.api.entity.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String password;
}
