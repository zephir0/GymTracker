package com.gymtracker.user;

import com.gymtracker.user.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableCaching
public interface UserRepository extends JpaRepository<User, Long> {
    @Cacheable(cacheNames = "users")
    Optional<User> findByLogin(String login);

    @Cacheable(cacheNames = "users")
    boolean existsByLoginOrEmailAddress(String login,
                                        String email);

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    void delete(User entity);

    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    <S extends User> S save(S entity);

}

