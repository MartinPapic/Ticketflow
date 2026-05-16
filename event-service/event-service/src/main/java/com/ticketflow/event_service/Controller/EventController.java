package com.ticketflow.event_service.Controller;

import com.ticketflow.event_service.DTO.EventDTO;
import com.ticketflow.event_service.Model.Event;
import com.ticketflow.event_service.Service.EventService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
@Slf4j
public class EventController {

    @Autowired
    private EventService service;

    @GetMapping
    public ResponseEntity<List<Event>> buscarTodos() {
        log.info("Buscando todos los eventos");
        List<Event> lista = service.buscarTodos();
        if(lista.isEmpty()){
            log.warn("No se encontraron eventos");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> buscarPorId(@PathVariable Long id) {
        log.info("Buscando evento con ID: {}", id);
        Event entity = service.buscarPorId(id);
        if (entity == null) {
            log.error("Evento con ID: {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<Event> crear(@Valid @RequestBody EventDTO dto) {
        log.info("Creando nuevo evento: {}", dto.getName());
        Event entity = new Event();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setVenueId(dto.getVenueId());
        entity.setDate(dto.getDate());
        entity.setStatus(dto.getStatus());
        
        Event saved = service.crear(entity);
        log.info("Evento creado con éxito. ID: {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando evento con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
