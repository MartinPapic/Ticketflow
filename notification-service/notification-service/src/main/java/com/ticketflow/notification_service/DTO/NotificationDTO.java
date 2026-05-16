package com.ticketflow.notification_service.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;

    @NotBlank(message = "El mensaje es obligatorio")
    private String message;

    @NotBlank(message = "El tipo de notificación es obligatorio")
    private String type;

    private String sentAt;
    private String status;
}
