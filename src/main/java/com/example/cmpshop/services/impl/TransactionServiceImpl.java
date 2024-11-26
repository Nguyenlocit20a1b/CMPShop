package com.example.cmpshop.services.impl;

import com.example.cmpshop.convertor.RSAEncryption;
import com.example.cmpshop.convertor.RSAKeyInitializer;
import com.example.cmpshop.dtos.TransactionEncryptDto;
import com.example.cmpshop.entities.TransactionHistory;
import com.example.cmpshop.exceptions.DecryptionException;
import com.example.cmpshop.exceptions.EncryptionException;
import com.example.cmpshop.exceptions.UnauthorizedException;
import com.example.cmpshop.repositories.TransactionHistoryRepository;
import com.example.cmpshop.services.ITransactionHistoryService;
import jakarta.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Implementation of the ITransactionHistoryService interface that handles
 * transaction creation and encryption/decryption of transaction data using RSA keys.
 * This service interacts with the database via the TransactionHistoryRepository.
 */
@Service
public class TransactionServiceImpl implements ITransactionHistoryService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private final RSAKeyInitializer keyInitializer;
    private final RSAEncryption rsaEncryption;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    public TransactionServiceImpl(RSAKeyInitializer keyInitializer, RSAEncryption rsaEncryption) {
        this.publicKey = keyInitializer.getPublicKey();
        this.privateKey = keyInitializer.getPrivateKey();
        this.keyInitializer = keyInitializer;
        this.rsaEncryption = rsaEncryption;
    }

    /**
     * Tạo một giao dịch mới và lưu vào cơ sở dữ liệu.
     *
     * @param transactionEncryptDto DTO chứa thông tin giao dịch đã mã hóa
     * @return TransactionHistory bản ghi giao dịch đã được giải mã và lưu
     * @throws DecryptionException  nếu xảy ra lỗi giải mã RSA
     * @throws PersistenceException nếu xảy ra lỗi khi lưu vào cơ sở dữ liệu
     */
    @Override
    public TransactionHistory createTransaction(TransactionEncryptDto transactionEncryptDto) {
        try {
            // decrypt with private key
            String transactionId = RSAEncryption.decrypt(transactionEncryptDto.getEncryptedTransactionId(), privateKey);
            String account = RSAEncryption.decrypt(transactionEncryptDto.getEncryptedAccount(), privateKey);
            String inDebt = RSAEncryption.decrypt(transactionEncryptDto.getEncryptedInDebt(), privateKey);
            String have = RSAEncryption.decrypt(transactionEncryptDto.getEncryptedHave(), privateKey);
            // bản ghi cho tài khoản nguồn
            TransactionHistory transaction = TransactionHistory.builder()
                    .transactionId(transactionId)
                    .account(account)
                    .inDebt(new BigDecimal(inDebt))
                    .have(new BigDecimal(have))
                    .build();
            transactionHistoryRepository.save(transaction);
            return transaction;
        } catch (GeneralSecurityException e) {
            logger.error("Transaction Error: Lỗi giải mã RSA. Transaction ID: ?, Account: ?, Error: {}",
                    e.getMessage());
            throw new DecryptionException("Lỗi giải mã RSA khi tạo giao dịch", e);
        } catch (Exception e) {
            logger.error("Transaction Error: Lỗi lưu cơ sở dữ liệu. Transaction ID: ?, Account: ?, Error: {}",
                    e.getMessage());
            throw new PersistenceException("Lỗi lưu cơ sở dữ liệu khi tạo giao dịch", e);
        }
    }

    /**
     * Chuẩn bị thông tin giao dịch để truyền giữa các service bằng cách mã hóa RSA.
     *
     * @param transactionId Mã giao dịch
     * @param account       Số tài khoản
     * @param inDebt        Số tiền nợ
     * @param have          Số tiền có
     * @return TransactionEncryptDto DTO chứa thông tin giao dịch đã mã hóa
     * @throws EncryptionException nếu xảy ra lỗi mã hóa RSA
     */
    @Override
    public TransactionEncryptDto prepareTransactionForService(String transactionId, String account, BigDecimal inDebt, BigDecimal have) {
        try {
            // Mã hóa RSA trước khi truyền qua service
            return new TransactionEncryptDto(
                    RSAEncryption.encrypt(transactionId, publicKey),
                    RSAEncryption.encrypt(account, publicKey),
                    RSAEncryption.encrypt(inDebt.toString(), publicKey),
                    RSAEncryption.encrypt(have.toString(), publicKey)
            );
        } catch (GeneralSecurityException e) {
            logger.error("Encryption Error: Transaction ID: ?, Account: ?, Error: {}", e.getMessage());
            throw new EncryptionException("Lỗi mã hóa RSA khi chuẩn bị thông tin giao dịch", e);
        }
    }
}
