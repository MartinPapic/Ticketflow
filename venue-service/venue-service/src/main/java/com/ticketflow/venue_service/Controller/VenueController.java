package com.ticketflow.venue_service.Controller;

import org.springframework.http.HttpStatus;

import com.ticketflow.venue_service.DTO.VenueDTO;
import com.ticketflow.venue_service.Model.Venue;
import com.ticketflow.venue_service.Service.VenueService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/venue")
@Slf4j
public class VenueController {

    @Autowired
    private VenueService service;

    @GetMapping
    public ResponseEntity<List<Venue>> buscarTodos() {
        log.info("Petición GET para listar todos los Venues");
        List<Venue> lista = service.buscarTodos();
        if (lista.isEmpty()) {
            log.warn("No se encontraron registros de Venue");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venue> buscarPorId(@PathVariable Long id) {
        log.info("Petición GET para obtener Venue con ID: {}", id);
        Venue entity = service.buscarPorId(id);
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<Venue> crear(@Valid @RequestBody VenueDTO dto) {
        log.info("Creando nuevo recinto: {}", dto.getName());
        Venue entity = new Venue();
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setCapacity(dto.getCapacity());
        
        Venue saved = service.crear(entity);
        log.info("Recinto creado exitosamente. ID: {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Petición DELETE para eliminar Venue con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
