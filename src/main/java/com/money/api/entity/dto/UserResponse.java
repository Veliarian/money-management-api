package com.money.api.entity.dto;

import com.money.api.entity.Role;
import com.money.api.entity.User;
import lombok.Data;

@Data
public class UserResponse {
    private String username;
    private Role role;

    public UserResponse(User user) {
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}
