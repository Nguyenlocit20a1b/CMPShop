package com.example.cmpshop.convertor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.KeyPair;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
@Component
public class RSAKeyInitializer {
    @Value("${keystorePath}")
    private String KEYSTORE_PATH;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct
    public void initializeKeys() throws Exception {
        File keyFile = new File(KEYSTORE_PATH);
        if (!keyFile.exists()) {
            // Generate and save RSA keys
            RSAEncryption.generateAndSaveKeys(KEYSTORE_PATH);
            System.out.println("RSA keys generated and saved to " + KEYSTORE_PATH);
        }
        // Load the keys
        KeyPair keyPair = RSAEncryption.loadKeys(KEYSTORE_PATH);
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
        System.out.println("RSA keys loaded successfully.");
    }
    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
