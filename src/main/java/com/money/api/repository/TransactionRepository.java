package com.money.api.repository;

import com.money.api.entity.Safe;
import com.money.api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySafe(Safe safe);
}
