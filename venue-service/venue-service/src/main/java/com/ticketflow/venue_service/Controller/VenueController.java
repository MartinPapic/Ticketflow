package com.ticketflow.venue_service.Controller;

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
        log.info("Buscando todos los recintos");
        List<Venue> lista = service.buscarTodos();
        if(lista.isEmpty()){
            log.warn("No se encontraron recintos");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venue> buscarPorId(@PathVariable Long id) {
        log.info("Buscando recinto con ID: {}", id);
        Venue entity = service.buscarPorId(id);
        if (entity == null) {
            log.error("Recinto con ID: {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
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
        log.info("Eliminando recinto con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
