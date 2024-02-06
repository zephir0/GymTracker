package com.gymtracker.training_session;

import com.gymtracker.user.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@EnableCaching
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    @Query("SELECT ts FROM TrainingSession ts " +
            "WHERE ts.trainingRoutine.id = :trainingRoutineId " +
            "AND ts.trainingDate = :trainingDate")
    Optional<TrainingSession> findByTrainingRoutineIdAndTrainingDate(
            @Param("trainingRoutineId") Long trainingRoutineId,
            @Param("trainingDate") LocalDateTime trainingDate
    );

    @Query("SELECT COUNT(ts) FROM TrainingSession ts WHERE ts.user = :user")
    Long countByUser(@Param("user") User user);

    @Cacheable(cacheNames = "training_sessions")
    @Query("SELECT ts FROM TrainingSession ts LEFT JOIN FETCH ts.trainingRoutine tr LEFT JOIN FETCH tr.user LEFT JOIN FETCH ts.trainingLogs tl LEFT JOIN FETCH tl.exercise WHERE ts.id = :id")
    Optional<TrainingSession> findById(@Param("id") Long id);

    @Query("SELECT DISTINCT ts FROM TrainingSession ts " + "JOIN FETCH ts.trainingRoutine tr " + "JOIN FETCH tr.user " + "WHERE ts.user = :user")
    List<TrainingSession> findTrainingSessionsByUser(User user);

    @Query("SELECT ts, SUM(tl.weight) as totalWeight FROM TrainingLog tl " + "JOIN tl.trainingSession ts " + "WHERE ts IN :trainingSessions " + "GROUP BY ts")
    List<Object[]> findTotalWeightsForTrainingSessions(@Param("trainingSessions") List<TrainingSession> trainingSessions);

    @Override
    @CacheEvict(cacheNames = "training_sessions", allEntries = true)
    void delete(TrainingSession entity);

    @Override
    @CacheEvict(cacheNames = "training_sessions", allEntries = true)
    void deleteById(Long aLong);

    @Override
    @CacheEvict(cacheNames = "training_sessions", allEntries = true)
    <S extends TrainingSession> S save(S entity);
}
