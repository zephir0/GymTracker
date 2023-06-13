package com.gymtracker.exercise;

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
    @Query("SELECT e FROM Exercise e WHERE e.description = :description AND (e.user.id = :userId OR (e.user.id IS NULL AND e.adminCreated = :isAdminCreated))")
    Optional<Exercise> findByDescriptionAndUserIdOrAdminCreated(@Param("description") String description,
                                                                @Param("userId") Long userId,
                                                                @Param("isAdminCreated") boolean isAdminCreated);


    @Cacheable(cacheNames = "exercises")
    List<Exercise> findAllByUserIdOrAdminCreated(Long userId,
                                                 boolean isAdminCreated);

    Exercise getReferenceById(Long exerciseId);

    Optional<Exercise> findByDescriptionAndAdminCreated(String description,
                                                        boolean isAdminCreated);


    @Override
    @CacheEvict(cacheNames = "exercises", allEntries = true)
    <S extends Exercise> S save(S entity);


    @Override
    @CacheEvict(cacheNames = "exercises", allEntries = true)
    void delete(Exercise entity);
}
