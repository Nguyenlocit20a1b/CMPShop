package com.example.cmpshop.convertor;
import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
public class AsymmetricEncryption  {
    private static final String ALGORITHM = "RSA";
    private static final String FULL_ALGORITHM = "SHA256withRSA";
    private static final String CIPHER_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final int KEY_SIZE = 2048;

    // Generate RSA Key Pair with SecureRandom
    public static KeyPair generateRsaKeyPair() throws GeneralSecurityException {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
            return keyPairGenerator.generateKeyPair();
        } catch (GeneralSecurityException e) {
            throw new GeneralSecurityException("Failed to generate RSA key pair", e);
        }
    }

    // Enhanced Encryption with OAEP Padding
    public static String encrypt(String data, PublicKey publicKey) throws GeneralSecurityException {
        if (data == null || publicKey == null) {
            throw new IllegalArgumentException("Data and public key must not be null");
        }
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (GeneralSecurityException e) {
            throw new GeneralSecurityException("Encryption failed", e);
        }
    }

    // Enhanced Decryption with OAEP Padding
    public static String decrypt(String encryptedData, PrivateKey privateKey) throws GeneralSecurityException {
        if (encryptedData == null || privateKey == null) {
            throw new IllegalArgumentException("Encrypted data and private key must not be null");
        }
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (GeneralSecurityException e) {
            throw new GeneralSecurityException("Decryption failed", e);
        }
    }
    // Digital Signature for Data
    public static String signData(String data, PrivateKey privateKey) throws GeneralSecurityException {
        if (data == null || privateKey == null) {
            throw new IllegalArgumentException("Data and private key must not be null");
        }
        try {
            Signature signature = Signature.getInstance(FULL_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(data.getBytes());
            byte[] signatureBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (GeneralSecurityException e) {
            throw new GeneralSecurityException("Signature generation failed", e);
        }
    }

    // Convert Public Key to Base64 String
    public static String getPublicKeyString(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    // Convert Private Key to Base64 String
    public static String getPrivateKeyString(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    // Reconstruct Public Key from Base64 String
    public static PublicKey getPublicKey(String publicKeyString) throws GeneralSecurityException {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return keyFactory.generatePublic(spec);
        } catch (GeneralSecurityException e) {
            throw new GeneralSecurityException("Failed to reconstruct public key", e);
        }
    }

    // Reconstruct Private Key from Base64 String
    public static PrivateKey getPrivateKey(String privateKeyString) throws GeneralSecurityException {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyString);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return keyFactory.generatePrivate(spec);
        } catch (GeneralSecurityException e) {
            throw new GeneralSecurityException("Failed to reconstruct private key", e);
        }
    }

    // Verify Data Signature
    public static boolean verifySignature(String data, String signatureStr, PublicKey publicKey) throws GeneralSecurityException {
        if (data == null || signatureStr == null || publicKey == null) {
            throw new IllegalArgumentException("Data, signature, and public key must not be null");
        }
        try {
            Signature signature = Signature.getInstance(FULL_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(data.getBytes());
            byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);
            return signature.verify(signatureBytes);
        } catch (GeneralSecurityException e) {
            throw new GeneralSecurityException("Signature verification failed", e);
        }
    }

    // Export KeyPair to PKCS12 Keystore


    // Import KeyPair from PKCS12 Keystore
    public static KeyPair importKeyPairFromPKCS12(String filepath, String password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream fis = new FileInputStream(filepath)) {
                keyStore.load(fis, password.toCharArray());

                PrivateKey privateKey = (PrivateKey) keyStore.getKey("asymmetric-keypair", password.toCharArray());
                PublicKey publicKey = keyStore.getCertificate("asymmetric-keypair").getPublicKey();

                return new KeyPair(publicKey, privateKey);
            }
        } catch (Exception e) {
            throw new GeneralSecurityException("Failed to import key pair from PKCS12", e);
        }
    }

}
