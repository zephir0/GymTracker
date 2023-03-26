package com.gymtracker.chat;

import com.gymtracker.ticket.Ticket;
import com.gymtracker.ticket.TicketNotFoundException;
import com.gymtracker.ticket.TicketRepository;
import com.gymtracker.user.User;
import com.gymtracker.user.UserRepository;
import com.gymtracker.user.UserRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelAuthorizationService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public boolean isUserAuthorized(String channelId,
                                    String username) {
        User user = userRepository.findByLogin(username).orElseThrow();

        if (channelId.equals("admin")) {
            return user.getUserRole().equals(UserRoles.ADMIN);
        } else {
            Ticket ticket = ticketRepository.findById(Long.valueOf(channelId)).orElseThrow(() -> new TicketNotFoundException("Ticket doesn't exist"));
            return username.equals(ticket.getAuthor().getLogin());
        }
    }
}
