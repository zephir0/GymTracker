package com.gymtracker.training_log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingLogRepository extends JpaRepository<TrainingLog, Long> {
    List<TrainingLog> findAllByExerciseId(Long id);
}
