package com.ticketflow.report_service.DTO;

import lombok.Data;

@Data
public class ReportDTO {
    private Long id;
    private String name;
    private String type;
    private String generatedAt;
    private String fileUrl;
}
