package com.example.cmpshop.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a Brand in the system.
 * Maps to the 'brands' table in the database.
 * This class utilizes Lombok annotations for boilerplate code reduction
 * and JPA annotations to define how it should be persisted.
 */
@Entity
@Table(name = "brand")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandEntity {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "Brand name cannot be blank")
    @Size(max = 255, message = "Brand name must not exceed 255 characters")
    @Column(nullable = false)
    private String name;
    @NotBlank(message = "Brand code cannot be blank")
    @Size(max = 100, message = "Brand code must not exceed 100 characters")
    @Column(nullable = false)
    private String code;
    private String description;
    @Column(nullable = false)
    private String origin; // xuất sứ
}
