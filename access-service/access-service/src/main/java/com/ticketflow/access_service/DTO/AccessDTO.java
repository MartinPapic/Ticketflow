package com.ticketflow.access_service.DTO;

import lombok.Data;

@Data
public class AccessDTO {
    private Long id;
    private Long ticketId;
    private String gate;
    private String accessTime;
    private String status;
}
