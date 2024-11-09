package com.example.cmpshop.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
/**
 * Entity representing a Category in the system.
 * Maps to the 'category' table in the database.
 * This class utilizes Lombok annotations for boilerplate code reduction
 * and JPA annotations to define how it should be persisted.
 */
@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryEntity {
    @Id
    @GeneratedValue
    private Long id ;
    @Column(nullable = false)
    @NotBlank(message = "Category name cannot be blank")
    @Size(max = 255, message = "Category name must not exceed 255 characters")
    private String name;
    @NotBlank(message = "Category code cannot be blank")
    @Size(max = 100, message = "Category code must not exceed 100 characters")
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String description;
    @OneToMany(mappedBy = "category" , cascade =  CascadeType.ALL)
    private List<CategoryTypeEntity> categoryTypes; // Join với bảng product 1-> n

}
