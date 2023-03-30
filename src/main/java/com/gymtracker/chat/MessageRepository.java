package com.gymtracker.chat;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableCaching
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Cacheable(cacheNames = "tickets")
    List<Message> findAllByTicketId(Long ticketId);

    @Override
    @CacheEvict(cacheNames = "tickets", allEntries = true)
    <S extends Message> S save(S entity);

    @Override
    @CacheEvict(cacheNames = "tickets", allEntries = true)
    void delete(Message entity);
}
