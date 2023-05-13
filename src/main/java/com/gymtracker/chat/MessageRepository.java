package com.gymtracker.chat;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    void delete(Message entity);

    @CacheEvict(cacheNames = "tickets", allEntries = true)
    @Modifying
    @Query("delete from Message messages where messages.ticket.id = :ticketId")
    void deleteAllByTicketId(@Param("ticketId") Long ticketId);
}
