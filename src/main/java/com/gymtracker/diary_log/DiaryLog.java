package com.gymtracker.diary_log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gymtracker.exercise.Exercise;
import com.gymtracker.gym_diary.GymDiary;
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
public class DiaryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reps")
    @NotNull(message = "diaryLog.reps.notNull")
    @PositiveOrZero
    private Integer reps;

    @Column(name = "weight")
    @NotNull(message = "diaryLog.weight.notNull")
    @PositiveOrZero
    private Integer weight;

    @Column(name = "personal_notes")
    private String personalNotes;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    @NotNull(message = "diaryLog.exerciseId.notNull")
    private Exercise exercise;

    @ManyToOne
    @NotNull(message = "diaryLog.gymDiaryId.notNull")
    @JoinColumn(name = "gym_diary_id")
    @JsonIgnore
    private GymDiary gymDiary;

}
