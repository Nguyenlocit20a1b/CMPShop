package com.example.cmpshop.convertor;
import jakarta.persistence.Convert;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Configuration
@Convert
public class AESEncryption implements AttributeConverter<String,String>{
    // Property name for the encryption password
    private static final String ENCRYPTION_PASSWORD_PROPERTY = "jasypt.encryptor.key";

    // Jasypt StringEncryptor để thực hiện mã hóa và giải mã
    private final StandardPBEStringEncryptor encryptor;
     /** Constructor for StringCryptoConverter.
      *
      * @param environment The Spring Environment used to access properties.
      */
    public AESEncryption(Environment environment) {
        // Initialize the encryptor with the encryption password from the environment
        this.encryptor = new StandardPBEStringEncryptor();
        this.encryptor.setPassword(environment.getProperty(ENCRYPTION_PASSWORD_PROPERTY));
    }
    /**
     * Chuyển đổi giá trị thuộc tính sang dạng được mã hóa.
     *
     * @param attribute Giá trị thuộc tính gốc sẽ được mã hóa.
     * @return Dạng được mã hóa của thuộc tính.
     */
    @Override
    public String convertToDatabaseColumn(String attribute) {
        return encryptor.encrypt(attribute);
    }

    /**
     * Chuyển đổi giá trị cơ sở dữ liệu được mã hóa sang dạng giải mã.
     *
     * @param dbData Giá trị được mã hóa được lưu trữ trong cơ sở dữ liệu.
     * @return Dạng giải mã của giá trị cơ sở dữ liệu.
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        return encryptor.decrypt(dbData);
    }

}
