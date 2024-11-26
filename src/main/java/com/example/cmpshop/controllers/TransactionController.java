package com.example.cmpshop.controllers;

import com.example.cmpshop.dtos.TransactionEncryptDto;
import com.example.cmpshop.entities.TransactionHistory;
import com.example.cmpshop.services.IProductService;
import com.example.cmpshop.services.ITransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private BigDecimal have = new BigDecimal(0);
    @Autowired
    private ITransactionHistoryService iTransactionHistoryService;

    @Autowired
    public TransactionController(ITransactionHistoryService iTransactionHistoryService) {
        this.iTransactionHistoryService = iTransactionHistoryService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> createTransaction(
            @RequestParam String transactionId,
            @RequestParam String sourceAccount,
            @RequestParam String targetAccount,
            @RequestParam BigDecimal amount
    ) {
        try {
            TransactionHistory sourceTransaction = iTransactionHistoryService.createTransaction(
                    transactionId, sourceAccount, amount, have
            );
            // Tạo giao dịch cho đích
            TransactionHistory targetTransaction = iTransactionHistoryService.createTransaction(
                    transactionId, targetAccount, have, amount
            );
            // Chuẩn bị dữ liệu để truyền giữa các service
            TransactionEncryptDto sourceTransactionDTO = iTransactionHistoryService.prepareTransactionForService(sourceTransaction);
            TransactionEncryptDto targetTransactionDTO = iTransactionHistoryService.prepareTransactionForService(targetTransaction);
            return ResponseEntity.ok(new Object[]{sourceTransactionDTO, targetTransactionDTO});
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
