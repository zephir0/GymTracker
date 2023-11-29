package com.gymtracker.ticket;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableCaching
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Override
    @Cacheable(cacheNames = "tickets")
    Optional<Ticket> findById(Long ticketId);

    List<Ticket> findAllByUserId(Long authorId);

    @Override
    @CacheEvict(cacheNames = "tickets", allEntries = true)
    <S extends Ticket> S save(S entity);

    @Override
    @CacheEvict(cacheNames = "tickets", allEntries = true)
    void delete(Ticket entity);
}
