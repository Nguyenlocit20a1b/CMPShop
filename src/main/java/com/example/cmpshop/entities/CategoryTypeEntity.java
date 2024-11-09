package com.example.cmpshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Entity representing a Category Type in the system.
 * Maps to the 'category_type' table in the database.
 * This class utilizes Lombok annotations for boilerplate code reduction
 * and JPA annotations to define how it should be persisted.
 */
@Entity
@Table(name = "categoty_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryTypeEntity {
    @Id
    @GeneratedValue
    private Long id ;
    @NotBlank(message = "Category Type name cannot be blank")
    @Size(max = 100, message = "Category Type name must not exceed 100 characters")
    @Column(nullable = false)
    private String name;
    @NotBlank(message = "Category Type code cannot be blank")
    @Size(max = 100, message = "Category Type code must not exceed 100 characters")
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private CategoryEntity category; // Join với bảng category n-> 1
}
