package com.money.api.controller;

import com.money.api.entity.Transaction;
import com.money.api.entity.User;
import com.money.api.entity.dto.TransactionRequest;
import com.money.api.entity.dto.TransactionResponse;
import com.money.api.service.SafeService;
import com.money.api.service.TransactionService;
import com.money.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/safes/{safeId}/transactions")
@RequiredArgsConstructor
public class TransactionsController {

    private final TransactionService transactionService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> addTransaction(@PathVariable Long safeId, @RequestBody TransactionRequest transactionRequest) {
        if (safeId == null) {
            return ResponseEntity.badRequest().body("Safe ID is required");
        }

        User user = userService.getCurrentUser();

        Transaction transaction = transactionService.createTransaction(
                safeId,
                user.getId(),
                transactionRequest.getAmount(),
                transactionRequest.getDescription()
        );

        TransactionResponse response = new TransactionResponse(transaction);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getTransactions(@PathVariable Long safeId) {
        if (safeId == null) {
            return ResponseEntity.badRequest().body("Safe ID is required");
        }

        User user = userService.getCurrentUser();

        List<Transaction> transactions = transactionService.getTransactionsBySafe(safeId, user.getId());
        List<TransactionResponse> transactionResponses = transactions.stream().map(TransactionResponse::new).toList();

        return ResponseEntity.status(HttpStatus.OK).body(transactionResponses);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<?> getTransaction(@PathVariable Long transactionId) {
        if (transactionId == null) {
            return ResponseEntity.badRequest().body("Transaction ID is required");
        }

        User user = userService.getCurrentUser();

        Transaction transaction = transactionService.getTransactionById(transactionId, user.getId());
        TransactionResponse response = new TransactionResponse(transaction);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{transactionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long transactionId) {
        if (transactionId == null) {
            return ResponseEntity.badRequest().body("Transaction ID is required");
        }

        User user = userService.getCurrentUser();
        transactionService.deleteTransaction(transactionId, user.getId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
