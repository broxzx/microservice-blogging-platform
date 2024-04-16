package com.example.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The UserEntity class represents the entity for a user in the system.
 * It is used to store and retrieve user data from the database.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "user_entity", schema = "public")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "email_constraint", unique = true)
    private String emailConstraint;

    @Column(name = "email_verified", unique = true)
    private Boolean emailVerified;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "federation_link")
    public String federationLink;

    @Column(name = "first_name")
    public String firstName;

    @Column(name = "last_name")
    public String lastName;

    @Column(name = "realm_id")
    public String realmId;

    @Column(name = "username")
    public String username;

    @Column(name = "created_timestamp")
    public Long createdTimestamp;

    @Column(name = "service_account_client_link")
    public String serviceAccountClientLink;

    @Column(name = "not_before")
    public Integer notBefore;

}
