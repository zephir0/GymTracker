package com.gymtracker.gym_diary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gymtracker.diary_log.DiaryLog;
import com.gymtracker.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime trainingDate;

    @Column(name = "training_name")
    @NotEmpty(message = "gymDiary.trainingName.notNull")
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "gymDiary")
    @JsonIgnore
    private List<DiaryLog> diaryLogs;
}
