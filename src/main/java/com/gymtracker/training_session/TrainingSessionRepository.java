package com.gymtracker.training_session;

import com.gymtracker.user.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableCaching
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
    @Cacheable(cacheNames = "training_sessions")
    List<TrainingSession> findAllByUser(User user);

    @Override
    @Cacheable(cacheNames = "training_sessions")
    Optional<TrainingSession> findById(Long aLong);

    @Override
    @CacheEvict(cacheNames = "training_sessions", allEntries = true)
    void delete(TrainingSession entity);

    @Override
    @CacheEvict(cacheNames = "training_sessions", allEntries = true)
    <S extends TrainingSession> S save(S entity);
}
