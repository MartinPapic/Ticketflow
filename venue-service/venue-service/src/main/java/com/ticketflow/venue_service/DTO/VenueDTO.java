package com.ticketflow.venue_service.DTO;

import lombok.Data;

@Data
public class VenueDTO {
    private Long id;
    private String name;
    private String address;
    private Integer capacity;
}
