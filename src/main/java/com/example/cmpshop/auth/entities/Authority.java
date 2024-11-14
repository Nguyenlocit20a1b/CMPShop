package com.example.cmpshop.auth.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Table(name = "Authority")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleTypes name;

    private String description;

    public Authority(RoleTypes admin, String s) {
    }

    @Override
    public String getAuthority() {
        /** this method needs to return a string
         as per the GrantedAuthority interface,
         therefore name() is called on the enum. */
        return name.name();
    }
}
