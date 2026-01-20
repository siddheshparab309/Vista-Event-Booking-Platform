package com.events.superapp.common.entity;

import com.events.superapp.modules.movies.entity.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    private String ssoProvider;

    private String ssoId;

    private String phoneNumber;

    private String preferredCity;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_USER;

    @CreationTimestamp
    private LocalDateTime createdAt;
}