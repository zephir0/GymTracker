package com.gymtracker.ticket;

import response_model.SuccessResponse;
import com.gymtracker.ticket.dto.TicketDto;
import com.gymtracker.ticket.dto.TicketResponseDto;
import com.gymtracker.ticket.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Api(tags = "Ticket API")
public class TicketController {
    private final TicketService ticketService;

    @PostMapping()
    @ApiOperation(value = "Create a new ticket")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse createTicket(@RequestBody @Validated TicketDto ticketDto) {
        ticketService.createTicket(ticketDto);
        return new SuccessResponse(HttpStatus.CREATED, "Ticket has been created.", LocalDateTime.now());
    }

    @GetMapping()
    @ApiOperation(value = "Get all tickets")
    @ResponseStatus(HttpStatus.OK)
    public List<TicketResponseDto> getAllTickets() {
        return ticketService.getAllTicketsForLoggedUser();
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Edit a ticket")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse editTicket(@PathVariable Long id,
                                      @RequestBody @Validated TicketDto ticketDto) {
        ticketService.editTicket(id, ticketDto);
        return new SuccessResponse(HttpStatus.OK, "Ticket has been edited.", LocalDateTime.now());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a ticket")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return new SuccessResponse(HttpStatus.OK, "Ticket deleted.", LocalDateTime.now());
    }
}
