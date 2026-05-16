package com.ticketflow.reservation_service.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationDTO {
    private Long id;

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El ID del evento es obligatorio")
    private Long eventId;

    @NotNull(message = "El ID del asiento es obligatorio")
    private Long seatId;

    private String status;
    private String expirationTime;
}
