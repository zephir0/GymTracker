package com.gymtracker.gym_diary;

import com.gymtracker.diary_log.DiaryLog;
import com.gymtracker.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "gym_diary")
public class GymDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "training_date")
    private LocalDateTime trainingDate;

    @Column(name = "training_name")
    @NotEmpty(message = "gymDiary.trainingName.notNull")
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    private List<DiaryLog> diaryLogs;
}
