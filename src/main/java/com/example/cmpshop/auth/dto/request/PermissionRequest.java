package com.example.cmpshop.auth.dto.request;

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
public class PermissionRequest {
    @NotEmpty(message = "Permission name cannot be empty")
    private String name;
    private  String endPoint;
    private String method;
}
