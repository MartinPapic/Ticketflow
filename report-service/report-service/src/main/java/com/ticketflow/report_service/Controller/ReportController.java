package com.ticketflow.report_service.Controller;

import com.ticketflow.report_service.DTO.ReportDTO;
import com.ticketflow.report_service.Model.Report;
import com.ticketflow.report_service.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

    @Autowired
    private ReportService service;

    @GetMapping
    public ResponseEntity<List<Report>> buscarTodos() {
        List<Report> lista = service.buscarTodos();
        if(lista.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> buscarPorId(@PathVariable Long id) {
        Report entity = service.buscarPorId(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<Report> crear(@RequestBody ReportDTO dto) {
        Report entity = new Report();
        // Here we'd map DTO to Entity. For simplicity, just saving new entity or manual map
        // assuming standard mapper logic
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setGeneratedAt(dto.getGeneratedAt());
        entity.setFileUrl(dto.getFileUrl());
        return ResponseEntity.ok(service.crear(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
