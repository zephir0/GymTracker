package com.gymtracker.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "email_address")
    private String emailAddress;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "user_role")
    private UserRoles userRole;
}
