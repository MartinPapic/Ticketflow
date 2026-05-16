package com.ticketflow.notification_service.DTO;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long userId;
    private String message;
    private String type;
    private String sentAt;
    private String status;
}
