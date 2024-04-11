package com.example.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "t_users", schema = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Long id;

    @Column(name = "c_username", unique = true)
    private String username;

    @Column(name = "c_password")
    private String password;

    @Column(name = "c_email", unique = true)
    private String email;

    @Column(name = "c_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @PrePersist
    public void init() {
        role = Role.ROLE_USER;
    }
}
