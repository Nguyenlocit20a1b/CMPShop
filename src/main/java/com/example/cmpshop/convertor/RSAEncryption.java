package com.example.cmpshop.convertor;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.cert.Certificate;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;
@Configuration
public class RSAEncryption {
    private static final String ALGORITHM = "RSA";
    private static final String FULL_ALGORITHM = "SHA256withRSA";
    private static final String CIPHER_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final int KEY_SIZE = 2048;
    private static final String KEYSTORE_PASSWORD = "Nguyenloc2002";
    private static final String KEY_ALIAS = "email_encryption_key";
    private static final String TYPE = "PKCS12";
    private static  final String ISSUERNAME = "CN=Self Signed Certificate";


    /**
     * Tạo cặp khóa RSA mới và lưu trữ vào tệp KeyStore dưới định dạng PKCS12.
     *
     * @param keystoreFilePath Đường dẫn tệp KeyStore để lưu trữ cặp khóa và chứng chỉ.
     * @throws Exception Nếu xảy ra lỗi khi tạo hoặc lưu trữ khóa.
     */
    public static void generateAndSaveKeys(String keystoreFilePath) throws Exception {
        // Generate RSA Key Pair
        KeyPair keyPair = generateRsaKeyPair();

        // Create self-signed certificate
        X509Certificate certificate = generateSelfSignedCertificate(keyPair);

        // Create PKCS12 KeyStore
        KeyStore keyStore = KeyStore.getInstance(TYPE);
        keyStore.load(null, null);

        // Store private key and certificate
        Certificate[] certChain = new Certificate[]{certificate};
        keyStore.setKeyEntry(
                KEY_ALIAS,
                keyPair.getPrivate(),
                KEYSTORE_PASSWORD.toCharArray(),
                certChain
        );

        // Save to file
        try (FileOutputStream fos = new FileOutputStream(keystoreFilePath)) {
            keyStore.store(fos, KEYSTORE_PASSWORD.toCharArray());
        }
    }

    /**
     * Tạo cặp khóa RSA với độ dài khóa được cấu hình.
     *
     * @return Đối tượng KeyPair chứa publicKey và privateKey.
     * @throws GeneralSecurityException Nếu xảy ra lỗi trong quá trình tạo khóa.
     */
    public static KeyPair generateRsaKeyPair() throws GeneralSecurityException {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
            return keyPairGenerator.generateKeyPair();
        } catch (GeneralSecurityException e) {
            throw new GeneralSecurityException("Failed to generate RSA key pair", e);
        }
    }

    /**
     * Mã hóa dữ liệu bằng thuật toán RSA với chế độ OAEP Padding.
     *
     * @param data      Dữ liệu đầu vào cần mã hóa.
     * @param publicKey Khóa công khai được sử dụng để mã hóa.
     * @return Chuỗi dữ liệu đã được mã hóa dưới dạng Base64.
     * @throws GeneralSecurityException Nếu xảy ra lỗi trong quá trình mã hóa.
     */
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

    /**
     * Giải mã dữ liệu đã mã hóa bằng thuật toán RSA với chế độ OAEP Padding.
     *
     * @param encryptedData Dữ liệu đã mã hóa dưới dạng Base64.
     * @param privateKey    Khóa riêng tư được sử dụng để giải mã.
     * @return Chuỗi dữ liệu gốc sau khi giải mã.
     * @throws GeneralSecurityException Nếu xảy ra lỗi trong quá trình giải mã.
     * @throws IllegalArgumentException Data and private key must not be null.
     */
    public static String decrypt(String encryptedData, PrivateKey privateKey) throws Exception {
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

    /**
     * Ký số dữ liệu bằng thuật toán RSA và SHA256.
     *
     * @param data       Dữ liệu cần ký số.
     * @param privateKey Khóa riêng tư được sử dụng để ký số.
     * @return Chuỗi chữ ký số dưới dạng Base64.
     * @throws GeneralSecurityException Nếu xảy ra lỗi trong quá trình ký số.
     * @throws IllegalArgumentException Data and private key must not be null.
     */
    public static String signData(String data, PrivateKey privateKey) throws Exception {
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

    /**
     * Tải cặp khóa RSA (gồm khóa công khai và khóa riêng tư) từ tệp KeyStore.
     *
     * @param keystoreFilePath Đường dẫn tệp KeyStore chứa cặp khóa.
     * @return Đối tượng KeyPair chứa khóa công khai và khóa riêng tư.
     * @throws Exception Nếu xảy ra lỗi trong quá trình tải khóa từ KeyStore.
     */
    public static KeyPair loadKeys(String keystoreFilePath) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(TYPE);
        try (FileInputStream fis = new FileInputStream(keystoreFilePath)) {
            keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());
        }

        // Get private key
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(
                KEY_ALIAS,
                KEYSTORE_PASSWORD.toCharArray()
        );

        // Get public key from certificate
        Certificate cert = keyStore.getCertificate(KEY_ALIAS);
        PublicKey publicKey = cert.getPublicKey();

        return new KeyPair(publicKey, privateKey);
    }

    /**
     * Tạo chứng chỉ tự ký (self-signed certificate) cho cặp khóa RSA.
     *
     * @param keyPair Cặp khóa RSA dùng để tạo chứng chỉ.
     * @return Đối tượng X509Certificate tự ký.
     * @throws Exception Nếu xảy ra lỗi trong quá trình tạo chứng chỉ.
     */
    private static X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
        X500Name issuerName = new X500Name(ISSUERNAME);
        BigInteger serialNumber = new BigInteger(64, new SecureRandom());
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 365 * 24 * 60 * 60 * 1000L); // 1 year validity

        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuerName,
                serialNumber,
                startDate,
                endDate,
                issuerName,
                keyPair.getPublic()
        );

        ContentSigner signer = new JcaContentSignerBuilder(FULL_ALGORITHM)
                .build(keyPair.getPrivate());

        return new JcaX509CertificateConverter()
                .getCertificate(certBuilder.build(signer));
    }
}
