package com.example.cmpshop.entities;

import com.example.cmpshop.convertor.AESEncryption;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Entity
@Table(name = "transaction_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionHistory  {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "Account cannot be blank")
    @Size(max = 255, message = "Account must not exceed 255 characters")
    @Column(name = "account", nullable = false)
    @Convert(converter = AESEncryption.class)
    private String account;
    @NotBlank(message = "Transaction Id cannot be blank")
    @Size(max = 255, message = "Transaction Id must not exceed 255 characters")
    @Column(name = "transaction_id", nullable = false)
    private String transactionId;
    @Column(name = "in_debt")
    private BigDecimal inDebt;
    @Column(name = "have")
    private BigDecimal have;
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @PrePersist
    protected void onCreate() {
        TimeZone vietnamTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        Calendar calendar = Calendar.getInstance(vietnamTimeZone);
        Date currentTime = calendar.getTime();
        this.createAt = currentTime;
    }

}
