package com.ticketflow.report_service.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReportDTO {
    private Long id;

    @NotBlank(message = "El nombre del reporte es obligatorio")
    private String name;

    @NotBlank(message = "El tipo de reporte es obligatorio")
    private String type;

    private String generatedAt;
    private String fileUrl;
}
