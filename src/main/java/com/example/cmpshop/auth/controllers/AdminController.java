package com.example.cmpshop.auth.controllers;

import com.example.cmpshop.auth.dto.request.RoleRequest;
import com.example.cmpshop.auth.dto.request.UpdateUserRolesRequest;
import com.example.cmpshop.auth.entities.RoleEntity;
import com.example.cmpshop.auth.entities.UserEntity;
import com.example.cmpshop.auth.services.impl.AuthorizationService;
import com.example.cmpshop.controllers.ProductController;
import com.example.cmpshop.exceptions.DataAccessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/admin")
@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "ADMIN_AUTH")
public class AdminController {
    @Autowired
    AuthorizationService authorityService;
    @Autowired
    UserDetailsService userDetailsService;
    /**
     * API Endpoint để tạo mới quyền (Authorization).
     *
     * @param request RoleRequest chứa thông tin quyền cần tạo (tên quyền và các quyền liên kết).
     * @return ResponseEntity chứa đối tượng RoleEntity vừa tạo hoặc lỗi.
     */
    @Operation(summary = "Create Authorization",
            description = "Admin can create a new Authorization by providing role name and permissions.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Authorization created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleEntity.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input parameter",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden, only Admin can access this endpoint",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Resound Not Found",
                    content = @Content)
    })
    @PostMapping("/create-author")
    public ResponseEntity<?> createAuthority (@RequestBody @Valid  RoleRequest request ){
        RoleEntity authority = authorityService.createAuthority(request);
        return  ResponseEntity.status(HttpStatus.CREATED).body(authority);
    }
    /**
     * API để cập nhật vai trò (roles) cho người dùng.
     *
     * @param request UpdateUserRolesRequest chứa thông tin email người dùng và danh sách vai trò mới.
     * @return ResponseEntity chứa thông tin người dùng sau khi cập nhật hoặc lỗi.
     */
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vai trò của người dùng đã được cập nhật thành công",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserEntity.class))}),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden, only Admin can access this endpoint",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng hoặc vai trò không tồn tại",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi không mong muốn trên máy chủ",
                    content = @Content)
    })
    @PutMapping("/update-user-roles")
    public ResponseEntity<?> updateUserRoles( @RequestBody  @Valid UpdateUserRolesRequest request) {
        try {
            UserEntity updatedUser = (UserEntity) authorityService.updateUserRoles(request);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn khác, trả về mã lỗi 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi không mong muốn: " + e.getMessage());
        }
    }
}
