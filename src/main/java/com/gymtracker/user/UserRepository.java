package com.gymtracker.user;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableCaching
public interface UserRepository extends CrudRepository<User, Long> {
    @Cacheable(cacheNames = "users")
    Optional<User> findByLogin(String login);

    @Cacheable(cacheNames = "users")
    List<User> findAllByUserRole(UserRoles userRole);

    @Cacheable(cacheNames = "users")
    boolean existsByLogin(String login);

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    void delete(User entity);

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    <S extends User> S save(S entity);
}
