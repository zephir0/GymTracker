package com.gymtracker.ticket;

import com.gymtracker.chat.Message;
import com.gymtracker.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Cacheable(cacheNames = "tickets")
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User author;

    @Column(name = "subject")
    @NotNull
    private String subject;

    @Column(name = "description")
    @NotNull
    private String description;

    @Column(name = "creation_date")
    @NotNull
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "ticket")
    private List<Message> messageList;
}
