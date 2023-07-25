package com.gymtracker.training_session;

import com.gymtracker.user.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableCaching
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    @Query("SELECT COUNT(ts) FROM TrainingSession ts WHERE ts.user = :user")
    Long countByUser(@Param("user") User user);

    @Cacheable(cacheNames = "training_sessions")
    @Query("SELECT ts FROM TrainingSession ts LEFT JOIN FETCH ts.trainingRoutine tr LEFT JOIN FETCH tr.user LEFT JOIN FETCH ts.trainingLogs tl LEFT JOIN FETCH tl.exercise WHERE ts.id = :id")
    Optional<TrainingSession> findById(@Param("id") Long id);

    @Query("SELECT ts, tl.id, SUM(tl.weight) as totalWeight FROM TrainingSession ts " +
            "JOIN FETCH ts.trainingLogs tl " +
            "JOIN FETCH ts.trainingRoutine tr " +
            "JOIN FETCH tr.user " +
            "JOIN FETCH tl.exercise " +
            "WHERE ts.user = :user " +
            "GROUP BY ts, tl.id, tl.weight")
    List<Object[]> findTrainingSessionsAndTotalWeightsByUser(User user);



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
