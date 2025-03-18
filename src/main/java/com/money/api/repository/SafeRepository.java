package com.money.api.repository;

import com.money.api.entity.Safe;
import com.money.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SafeRepository extends JpaRepository<Safe, Long> {
    List<Safe> findByUsersContains(User user);
}
