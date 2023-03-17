package com.gymtracker.exercise;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gymtracker.diary_log.DiaryLog;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "exercise")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private Long id;

    @Column(name = "description")
    @NotEmpty(message = "exercise.description.notNull")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotEmpty(message = "exercise.muscleGroup.notNull")
    @Column(name = "muscle_group")

    private MuscleGroup muscleGroup;
    @OneToMany(mappedBy = "exercise")
    @JsonIgnore
    private List<DiaryLog> diaryLogList;
}
