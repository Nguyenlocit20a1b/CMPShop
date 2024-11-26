package com.example.cmpshop.services;

import com.example.cmpshop.dtos.TransactionEncryptDto;
import com.example.cmpshop.entities.TransactionHistory;

import java.math.BigDecimal;

public interface ITransactionHistoryService {
    TransactionHistory  createTransaction (String transactionId, String account, BigDecimal inDebt, BigDecimal have);
    TransactionEncryptDto prepareTransactionForService(TransactionHistory transactionHistory);
}
