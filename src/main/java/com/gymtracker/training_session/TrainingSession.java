package com.gymtracker.training_session;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gymtracker.training_log.TrainingLog;
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
@Table(name = "training_session")
public class TrainingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "training_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime trainingDate;

    @Column(name = "training_name")
    @NotEmpty(message = "trainingSession.trainingName.notNull")
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "trainingSession")
    private List<TrainingLog> trainingLogs;
}
