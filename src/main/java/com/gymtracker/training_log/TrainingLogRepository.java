package com.gymtracker.training_log;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableCaching
public interface TrainingLogRepository extends JpaRepository<TrainingLog, Long> {
    @Cacheable(cacheNames = "training_logs")
    @Query("SELECT t FROM TrainingLog t JOIN FETCH t.exercise e JOIN FETCH e.user u1 JOIN FETCH t.trainingSession ts JOIN FETCH ts.user u2 WHERE t.exercise.id = :exerciseId")
    List<TrainingLog> findAllByExerciseId(@Param("exerciseId") Long exerciseId);


    @Override
    @CacheEvict(cacheNames = "training_logs", allEntries = true)
    <S extends TrainingLog> S save(S entity);

    @Override
    @Cacheable(cacheNames = "training_logs")
    Optional<TrainingLog> findById(Long aLong);

    @Override
    @CacheEvict(cacheNames = "training_logs", allEntries = true)
    void delete(TrainingLog entity);
}
