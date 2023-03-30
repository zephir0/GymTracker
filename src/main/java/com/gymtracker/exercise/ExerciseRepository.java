package com.gymtracker.exercise;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableCaching
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    @Cacheable(cacheNames = "exercises")
    List<Exercise> findAllByUserIdOrAdminCreated(Long userId,
                                                 boolean isAdminCreated);

    Optional<Exercise> getReferenceByDescription(String description);

    @Override
    @CacheEvict(cacheNames = "exercises", allEntries = true)
    <S extends Exercise> S save(S entity);

    @Override
    @CacheEvict(cacheNames = "exercises", allEntries = true)
    void delete(Exercise entity);
}
