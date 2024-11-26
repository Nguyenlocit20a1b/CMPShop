package com.example.cmpshop.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEncryptDto {
    private String encryptedTransactionId;
    private String encryptedAccount;
    private String encryptedInDebt;
    private String encryptedHave;
    private String encryptedTime;
}
