package com.money.api.service;

import com.money.api.entity.Safe;
import com.money.api.entity.Transaction;
import com.money.api.entity.User;
import com.money.api.repository.SafeRepository;
import com.money.api.repository.TransactionRepository;
import com.money.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final SafeRepository safeRepository;
    private final UserRepository userRepository;

    public Transaction createTransaction(Long safeId, Long userId, Double amount, String description) {
        Safe safe = safeRepository.findById(safeId)
                .orElseThrow(() -> new EntityNotFoundException("Safe with id " + safeId + " not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

        if(!safe.getUsers().contains(user)) {
            throw new AccessDeniedException("User " + user + " does not have access to transaction");
        }

        Transaction transaction = Transaction.builder()
                .description(description)
                .amount(amount)
                .time(LocalDateTime.now())
                .userId(userId)
                .safe(safe)
                .build();

        safe.setAmount(safe.getAmount() + amount);
        safeRepository.save(safe);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsBySafe(Long safeId, Long userId) {
        Safe safe = safeRepository.findById(safeId)
                .orElseThrow(() -> new EntityNotFoundException("Safe with id " + safeId + " not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

        if(!safe.getUsers().contains(user)) {
            throw new AccessDeniedException("User " + user + " does not have access to transaction");
        }

        return transactionRepository.findBySafe(safe);
    }

    public Transaction getTransactionById(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction with id " + transactionId + " not found"));

        Safe safe = transaction.getSafe();
        if(safe.getUsers().stream().noneMatch(user -> user.getId().equals(userId))) {
            throw new AccessDeniedException("User " + userId + " does not have access to transaction");
        }

        return transaction;
    }

    public void deleteTransaction(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction with id " + transactionId + " not found"));

        Safe safe = transaction.getSafe();
        if(safe.getUsers().stream().noneMatch(user -> user.getId().equals(userId))) {
            throw new AccessDeniedException("User " + userId + " does not have access to transaction");
        }

        safe.setAmount(safe.getAmount() - transaction.getAmount());
        safeRepository.save(safe);

        transactionRepository.delete(transaction);
    }
}
