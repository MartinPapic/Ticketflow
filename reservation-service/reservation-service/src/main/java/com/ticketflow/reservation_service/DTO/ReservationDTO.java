package com.ticketflow.reservation_service.DTO;

import lombok.Data;

@Data
public class ReservationDTO {
    private Long id;
    private Long userId;
    private Long eventId;
    private Long seatId;
    private String status;
    private String expirationTime;
}
