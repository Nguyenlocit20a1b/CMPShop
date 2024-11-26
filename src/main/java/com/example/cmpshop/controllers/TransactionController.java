package com.example.cmpshop.controllers;

import com.example.cmpshop.auth.entities.RoleEntity;
import com.example.cmpshop.dtos.TransactionEncryptDto;
import com.example.cmpshop.entities.TransactionHistory;
import com.example.cmpshop.services.IProductService;
import com.example.cmpshop.services.ITransactionHistoryService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.NamedBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.PublicKey;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "TRANSACTION")
public class TransactionController {
    private BigDecimal have = new BigDecimal(0);
    @Autowired
    private ITransactionHistoryService iTransactionHistoryService;

    @Autowired
    public TransactionController(ITransactionHistoryService iTransactionHistoryService) {
        this.iTransactionHistoryService = iTransactionHistoryService;
    }

    /**
     * API endpoint để thực hiện chuyển tiền giữa hai tài khoản.
     *
     * @param transactionId ID của giao dịch, đảm bảo duy nhất
     * @param sourceAccount Tài khoản nguồn thực hiện chuyển tiền
     * @param targetAccount Tài khoản đích nhận tiền
     * @param amount        Số tiền cần chuyển
     * @return ResponseEntity chứa thông tin giao dịch hoặc thông báo lỗi
     */
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "400", description = "Lỗi lưu cơ sở dữ liệu khi tạo giao dịch",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi mãy chủ",
                    content = @Content)
    })
    @PostMapping("/transfer")
    public ResponseEntity<?> createTransaction(
            @RequestParam String transactionId,
            @RequestParam String sourceAccount,
            @RequestParam String targetAccount,
            @RequestParam BigDecimal amount
    ) {
        try {
            // Chuẩn bị dữ liệu để truyền giữa các service
            TransactionEncryptDto sourceTransactionDTO = iTransactionHistoryService.prepareTransactionForService(transactionId, sourceAccount, amount, have);
            TransactionEncryptDto targetTransactionDTO = iTransactionHistoryService.prepareTransactionForService(transactionId, targetAccount, have, amount);
            // Tạo giao dịch cho nguồn
            TransactionHistory sourceTransaction = iTransactionHistoryService.createTransaction(
                    sourceTransactionDTO
            );
            // Tạo giao dịch cho đích
            TransactionHistory targetTransaction = iTransactionHistoryService.createTransaction(
                    targetTransactionDTO
            );
            return ResponseEntity.ok(new Object[]{sourceTransaction, targetTransaction});
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
