package com.gymtracker.exercise;

import com.gymtracker.exercise.entity.Exercise;
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
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    @Query("SELECT DISTINCT e FROM Exercise e LEFT JOIN FETCH e.user WHERE e.name = :name AND (e.user.id = :userId OR (e.user.id IS NULL AND e.adminCreated = :isAdminCreated))")
    Optional<Exercise> findByExerciseNameAndUserIdOrAdminCreated(@Param("name") String name,
                                                                 @Param("userId") Long userId,
                                                                 @Param("isAdminCreated") boolean isAdminCreated);

    @Cacheable(cacheNames = "exercises")
    List<Exercise> findAllByUserIdOrAdminCreated(Long userId,
                                                 boolean isAdminCreated);

    Exercise getReferenceById(Long exerciseId);

    Optional<Exercise> findByNameAndAdminCreated(String name,
                                                 boolean isAdminCreated);


    @Override
    @CacheEvict(cacheNames = "exercises", allEntries = true)
    <S extends Exercise> S save(S entity);


    @Override
    @CacheEvict(cacheNames = "exercises", allEntries = true)
    void delete(Exercise entity);
}
