package com.gymtracker.exercise;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gymtracker.training_log.TrainingLog;
import com.gymtracker.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "exercise")
@Cacheable(cacheNames = "exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    @NotEmpty(message = "exercise.description.notEmpty")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "exercise.muscleGroup.notNull")
    @Column(name = "muscle_group")
    private MuscleGroup muscleGroup;

    @OneToMany(mappedBy = "exercise")
    private List<TrainingLog> trainingLogList;

    @Column(name = "admin_created")
    private boolean adminCreated;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
