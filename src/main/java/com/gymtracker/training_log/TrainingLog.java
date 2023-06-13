package com.gymtracker.training_log;

import com.gymtracker.exercise.Exercise;
import com.gymtracker.training_session.TrainingSession;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Cacheable(cacheNames = "training_logs")
@Table(name = "training_log")
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
    @JoinColumn(name = "training_session_id")
    @NotNull(message = "trainingLog.trainingSessionId.notNull")
    private TrainingSession trainingSession;

}
