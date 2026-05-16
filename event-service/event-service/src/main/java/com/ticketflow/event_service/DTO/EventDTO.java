package com.ticketflow.event_service.DTO;

import lombok.Data;

@Data
public class EventDTO {
    private Long id;
    private String name;
    private String description;
    private Long venueId;
    private String date;
    private String status;
}
