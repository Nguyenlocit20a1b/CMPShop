package com.example.cmpshop.services.impl;

import com.example.cmpshop.convertor.AESEncryption;
import com.example.cmpshop.convertor.RSAEncryption;
import com.example.cmpshop.dtos.TransactionEncryptDto;
import com.example.cmpshop.entities.TransactionHistory;
import com.example.cmpshop.repositories.TransactionHistoryRepository;
import com.example.cmpshop.services.ITransactionHistoryService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDate;
import java.util.Date;


@Service
public class TransactionServiceImpl implements ITransactionHistoryService {
    @Value("${keystorePath}")
    private String KEYSTORE_PATH;
    @Value("${keystorePassword}")
    private String keystorePassword;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    private final AESEncryption aesEncryption;

    private final RSAEncryption rsaEncryption;
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    @Autowired
    public TransactionServiceImpl(AESEncryption aesEncryption, RSAEncryption rsaEncryption) {
        this.aesEncryption = aesEncryption;
        this.rsaEncryption = rsaEncryption;
    }

    @PostConstruct
    public void initializeKeys() throws Exception {
        if (!new File(KEYSTORE_PATH).exists()) {
            RSAEncryption.generateAndSaveKeys(KEYSTORE_PATH);
        }
        KeyPair keyPair = RSAEncryption.loadKeys(KEYSTORE_PATH);
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }
    @Override
    public TransactionHistory createTransaction(String transactionId, String account, BigDecimal inDebt, BigDecimal have ) {
        try{
            String encryptedAccount = aesEncryption.convertToDatabaseColumn(account);
            // bản gi cho tài khoản nguồn
        TransactionHistory sourceTransaction = TransactionHistory.builder()
                .transactionId(transactionId)
                .account(encryptedAccount)
                .inDebt(inDebt)
                .have(have)
                .build();
        transactionHistoryRepository.save(sourceTransaction);
            // bản gi cho tài khoản đích
        TransactionHistory targetTransaction  = TransactionHistory.builder()
                .transactionId(transactionId)
                .account(encryptedAccount)
                .inDebt(inDebt)
                .have(have)
                .build();

        transactionHistoryRepository.save(targetTransaction);
        return sourceTransaction;
        }
        catch (Exception e) {
            logger.error("Transaction Error: Transaction ID: ?, Account: ?, InDebt: ?, Have: ?, Error: {}", e.getMessage());
            throw new RuntimeException("Transaction creation failed", e);
        }
    }

    @Override
    public TransactionEncryptDto prepareTransactionForService(TransactionHistory transactionHistory) {
        try {
            // Giải mã số tài khoản trước khi mã hóa RSA
            String decryptedAccount = aesEncryption.convertToEntityAttribute(transactionHistory.getAccount());
            return new TransactionEncryptDto(
                    RSAEncryption.encrypt(transactionHistory.getTransactionId(), publicKey),
                    RSAEncryption.encrypt(decryptedAccount, publicKey),
                    RSAEncryption.encrypt(transactionHistory.getInDebt().toString(), publicKey),
                    RSAEncryption.encrypt(transactionHistory.getHave().toString(), publicKey),
                    RSAEncryption.encrypt(transactionHistory.getCreateAt().toString(), publicKey)
                    );
        } catch (GeneralSecurityException e) {
            logger.error("Encryption Error: Transaction ID: ?, Account: ?, Error: {}", e.getMessage());
            throw new RuntimeException("Transaction encryption failed", e);
        }
    }
}
