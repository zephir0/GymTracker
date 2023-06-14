package com.gymtracker.training_routine;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gymtracker.exercise.Exercise;
import com.gymtracker.training_session.TrainingSession;
import com.gymtracker.user.entity.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "training_routine")
public class TrainingRoutine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "routine_name")
    @NotNull
    private String routineName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    @JsonBackReference
    private User user;

    @ManyToMany()
    @JoinTable(
            name = "training_routine_exercise",
            joinColumns = @JoinColumn(name = "training_routine_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id"))
    private List<Exercise> exerciseList = new ArrayList<>();

    @OneToMany(mappedBy = "trainingRoutine")
    @JsonIgnore
    private List<TrainingSession> trainingSessionList = new ArrayList<>();

    public void addExercise(Exercise exercise) {
        exerciseList.add(exercise);
        exercise.getTrainingRoutines().add(this);
    }

    public void removeExercise(Exercise exercise) {
        exerciseList.remove(exercise);
        exercise.getTrainingRoutines().remove(this);
    }
}
