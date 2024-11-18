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
public class RoleRequest {
    @NotEmpty(message = "Role name cannot be empty")
    private String name;
    private  String description;
    private Set<String> permissionNames;
}
