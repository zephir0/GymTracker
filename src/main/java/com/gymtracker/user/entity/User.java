package com.gymtracker.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gymtracker.exercise.Exercise;
import com.gymtracker.ticket.Ticket;
import com.gymtracker.training_routine.TrainingRoutine;
import com.gymtracker.training_session.TrainingSession;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cache.annotation.Cacheable;
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
@Cacheable(cacheNames = "users")
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "user")
    private List<TrainingSession> trainingSessions;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<TrainingRoutine> trainingRoutines;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Exercise> exerciseList;

    @OneToMany(mappedBy = "author")
    private List<Ticket> ticketList;

}
