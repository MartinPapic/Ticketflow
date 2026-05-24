package com.ticketflow.report_service.Controller;

import org.springframework.http.HttpStatus;

import com.ticketflow.report_service.DTO.ReportDTO;
import com.ticketflow.report_service.Model.Report;
import com.ticketflow.report_service.Service.ReportService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/report")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService service;

    @GetMapping
    public ResponseEntity<List<Report>> buscarTodos() {
        log.info("Petición GET para listar todos los Reports");
        List<Report> lista = service.buscarTodos();
        if (lista.isEmpty()) {
            log.warn("No se encontraron registros de Report");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> buscarPorId(@PathVariable Long id) {
        log.info("Petición GET para obtener Report con ID: {}", id);
        Report entity = service.buscarPorId(id);
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<Report> crear(@Valid @RequestBody ReportDTO dto) {
        log.info("Generando reporte: {} del tipo {}", dto.getName(), dto.getType());
        Report entity = new Report();
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setGeneratedAt(dto.getGeneratedAt());
        entity.setFileUrl(dto.getFileUrl());
        
        Report saved = service.crear(entity);
        log.info("Reporte registrado exitosamente. ID: {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Report> actualizar(@PathVariable Long id, @Valid @RequestBody ReportDTO dto) {
        log.info("Petición PUT para actualizar Report con ID: {}", id);
        Report entity = new Report();
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setGeneratedAt(dto.getGeneratedAt());
        entity.setFileUrl(dto.getFileUrl());
        
        Report updated = service.actualizar(id, entity);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Petición DELETE para eliminar Report con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
