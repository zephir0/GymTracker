package com.gymtracker.exercise.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gymtracker.training_log.TrainingLog;
import com.gymtracker.training_routine.TrainingRoutine;
import com.gymtracker.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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

    @Column(name = "name")
    @NotEmpty
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "muscle_group")
    private MuscleGroup muscleGroup;

    @OneToMany(mappedBy = "exercise")
    @JsonIgnore
    private List<TrainingLog> trainingLogList;

    @Column(name = "admin_created")
    private boolean adminCreated;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToMany(mappedBy = "exerciseList")
    @JsonIgnore
    private List<TrainingRoutine> trainingRoutines = new ArrayList<>();
}
