package com.gymtracker.training_log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gymtracker.exercise.Exercise;
import com.gymtracker.training_session.TrainingSession;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "diary_logs")
public class TrainingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reps")
    @NotNull(message = "trainingLog.reps.notNull")
    @PositiveOrZero
    private Integer reps;

    @Column(name = "weight")
    @NotNull(message = "trainingLog.weight.notNull")
    @PositiveOrZero
    private Integer weight;

    @Column(name = "personal_notes")
    private String personalNotes;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    @NotNull(message = "trainingLog.exerciseId.notNull")
    private Exercise exercise;

    @ManyToOne
    @NotNull(message = "trainingLog.trainingSessionId.notNull")
    @JoinColumn(name = "training_session_id")
    @JsonIgnore
    private TrainingSession trainingSession;

}
