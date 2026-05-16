package com.ticketflow.ticket_service.DTO;

import lombok.Data;

@Data
public class TicketDTO {
    private Long id;
    private Long orderId;
    private Long eventId;
    private Long seatId;
    private Double price;
    private String status;
}
