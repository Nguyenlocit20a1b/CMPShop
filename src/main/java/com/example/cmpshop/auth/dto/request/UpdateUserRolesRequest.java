package com.example.cmpshop.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRolesRequest {
    @NotEmpty(message = "Role name cannot be empty")
    @Email(message = "Email should be valid")
    private String email ;
    private Set<String> roleNames;  // Danh sách tên các vai trò mà admin muốn gán cho người dùng
}