package com.gymtracker.training_routine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingRoutineRepository extends JpaRepository<TrainingRoutine, Long> {

    @Query("SELECT tr FROM TrainingRoutine tr JOIN FETCH tr.exerciseList e  WHERE tr.id = :id")
    Optional<TrainingRoutine> findByTrainingRoutineId(@Param("id") Long id);


}
