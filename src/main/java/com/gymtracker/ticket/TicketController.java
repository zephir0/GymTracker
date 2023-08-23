package com.gymtracker.ticket;

import com.gymtracker.response.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<SuccessResponse> createTicket(@RequestBody @Validated TicketDto ticketDto) {
        ticketService.createTicket(ticketDto);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.CREATED, "Ticket has been created.", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
    }

    @GetMapping()
    @ApiOperation(value = "Get all tickets")
    public ResponseEntity<List<TicketResponseDto>> getAllTickets() {
        List<TicketResponseDto> ticketList = ticketService.getAllTicketsForLoggedUser();
        return new ResponseEntity<>(ticketList, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Edit a ticket")
    public ResponseEntity<SuccessResponse> editTicket(@PathVariable Long id,
                                                      @RequestBody @Validated TicketDto ticketDto) {
        ticketService.editTicket(id, ticketDto);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, "Ticket has been edited.", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a ticket")
    public ResponseEntity<SuccessResponse> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, "Ticket deleted.", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }
}
