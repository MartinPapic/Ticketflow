package com.ticketflow.user_service.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;
    private Boolean active;
    private String createdAt;
    private String updatedAt;
}
