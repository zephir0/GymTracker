package com.gymtracker.user;

import com.gymtracker.gym_diary.GymDiary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Email(message = "user.email.validation")
    @NotNull(message = "user.email.notNull")
    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "login")
    @NotEmpty(message = "user.login.notNull")
    private String login;

    @Column(name = "password")
    @NotEmpty(message = "user.password.notNull")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRoles userRole;

    @CreatedDate
    @Column
    private LocalDateTime creationDate;

    @OneToMany
    private List<GymDiary> gymDiaries;

}
