package com.example.cmpshop.services;

import com.example.cmpshop.dtos.TransactionEncryptDto;
import com.example.cmpshop.entities.TransactionHistory;
import com.example.cmpshop.exceptions.DecryptionException;
import jakarta.persistence.PersistenceException;
import org.bouncycastle.openssl.EncryptionException;

import java.math.BigDecimal;

public interface ITransactionHistoryService {
    /**
     * Tạo một giao dịch mới và lưu vào cơ sở dữ liệu.
     *
     * @param transactionEncryptDto DTO chứa thông tin giao dịch đã mã hóa
     * @return TransactionHistory bản ghi giao dịch đã được giải mã và lưu
     */
    TransactionHistory  createTransaction (TransactionEncryptDto  transactionEncryptDto);
    /**
     * Chuẩn bị thông tin giao dịch để truyền giữa các service bằng cách mã hóa RSA.
     *
     * @param transactionId Mã giao dịch
     * @param account Số tài khoản
     * @param inDebt Số tiền nợ
     * @param have Số tiền có
     * @return TransactionEncryptDto DTO chứa thông tin giao dịch đã mã hóa
     */
    TransactionEncryptDto prepareTransactionForService(String transactionId, String account, BigDecimal inDebt, BigDecimal have);
}
