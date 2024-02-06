package com.gymtracker.training_session;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gymtracker.training_log.TrainingLog;
import com.gymtracker.training_routine.TrainingRoutine;
import com.gymtracker.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Cacheable(cacheNames = "training_sessions")
@Table(name = "training_session")
public class TrainingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "training_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime trainingDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "trainingSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingLog> trainingLogs;

    @ManyToOne
    @JoinColumn(name = "routine_id")
    private TrainingRoutine trainingRoutine;
}
