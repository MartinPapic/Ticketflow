package com.ticketflow.event_service.Controller;

import org.springframework.http.HttpStatus;

import com.ticketflow.event_service.DTO.EventDTO;
import com.ticketflow.event_service.Model.Event;
import com.ticketflow.event_service.Service.EventService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Event", description = "Operaciones del microservicio de Event")
@RestController
@RequestMapping("/api/v1/event")
@Slf4j
@Tag(name = "Event", description = "API for Event operations")
public class EventController {

    @Autowired
    private EventService service;

    @Operation(summary = "Obtener todos los registros", description = "Retorna una lista completa de entidades")
    @GetMapping
    public ResponseEntity<List<Event>> buscarTodos() {
        log.info("Petición GET para listar todos los Events");
        List<Event> lista = service.buscarTodos();
        if (lista.isEmpty()) {
            log.warn("No se encontraron registros de Event");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Buscar por ID", description = "Busca un registro específico por su identificador único")
    @GetMapping("/{id}")
    public ResponseEntity<Event> buscarPorId(@PathVariable Long id) {
        log.info("Petición GET para obtener Event con ID: {}", id);
        Event entity = service.buscarPorId(id);
        return ResponseEntity.ok(entity);
    }

    @Operation(summary = "Crear nuevo registro", description = "Registra una nueva entidad en la base de datos")
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

    @Operation(summary = "Actualizar registro", description = "Actualiza completamente un registro existente")
    @PutMapping("/{id}")
    public ResponseEntity<Event> actualizar(@PathVariable Long id, @Valid @RequestBody EventDTO dto) {
        log.info("Petición PUT para actualizar Event con ID: {}", id);
        Event entity = new Event();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setVenueId(dto.getVenueId());
        entity.setDate(dto.getDate());
        entity.setStatus(dto.getStatus());
        
        Event updated = service.actualizar(id, entity);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar registro", description = "Elimina un registro de la base de datos por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Petición DELETE para eliminar Event con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
