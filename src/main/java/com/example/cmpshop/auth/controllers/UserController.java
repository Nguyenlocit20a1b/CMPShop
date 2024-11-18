package com.example.cmpshop.auth.controllers;

import com.example.cmpshop.auth.dto.response.UserDetailDTO;
import com.example.cmpshop.auth.entities.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    @Autowired
    private UserDetailsService userDetailsService;
    /**
     * Lấy thông tin hồ sơ người dùng đã xác thực.
     *
     * @param principal đại diện cho người dùng đã xác thực (thông qua JWT hoặc phiên làm việc)
     * @return ResponseEntity chứa thông tin chi tiết của người dùng (UserDetailDTO) nếu người dùng hợp lệ, hoặc trả về mã trạng thái UNAUTHORIZED nếu không tìm thấy người dùng.
     */
    @Operation(summary = "Get User Profile", description = "Retrieve the profile details of the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched user profile.",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDetailDTO.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - User not authenticated.",
                    content = @Content)
    })
    @SecurityRequirements()
    @GetMapping("/profile")
    public ResponseEntity<UserDetailDTO>  getUserProfile (Principal principal) {
        // principal 1 interface đại diện cho người dùng đã xac thuc
        UserEntity user = (UserEntity) userDetailsService.loadUserByUsername(principal.getName());
        if(null == user) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserDetailDTO userDetailDTO = UserDetailDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .authorityList(user.getAuthorities().toArray())
                .build();
        return new ResponseEntity<>(userDetailDTO, HttpStatus.OK);
    };
}
