package com.ticketflow.ticket_service.Controller;

import com.ticketflow.ticket_service.DTO.TicketDTO;
import com.ticketflow.ticket_service.Model.Ticket;
import com.ticketflow.ticket_service.Service.TicketService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Ticket", description = "Operaciones del microservicio de Ticket")
@RestController
@RequestMapping("/api/v1/ticket")
@Slf4j
@Tag(name = "Ticket", description = "API for Ticket operations")
public class TicketController {

    @Autowired
    private TicketService service;

    @Operation(summary = "Obtener todos los registros", description = "Retorna una lista completa de entidades")
    @GetMapping
    public ResponseEntity<List<Ticket>> buscarTodos() {
        log.info("Buscando todos los tickets");
        List<Ticket> lista = service.buscarTodos();
        if(lista.isEmpty()){
            log.warn("No se encontraron tickets");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Buscar por ID", description = "Busca un registro específico por su identificador único")
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> buscarPorId(@PathVariable Long id) {
        log.info("Buscando ticket con ID: {}", id);
        Ticket entity = service.buscarPorId(id);
        if (entity == null) {
            log.error("Ticket con ID: {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    @Operation(summary = "Crear nuevo registro", description = "Registra una nueva entidad en la base de datos")
    @PostMapping
    public ResponseEntity<Ticket> crear(@Valid @RequestBody TicketDTO dto) {
        log.info("Creando nuevo ticket para el evento ID: {}", dto.getEventId());
        Ticket entity = new Ticket();
        entity.setOrderId(dto.getOrderId());
        entity.setEventId(dto.getEventId());
        entity.setSeatId(dto.getSeatId());
        entity.setPrice(dto.getPrice());
        entity.setStatus(dto.getStatus());
        
        Ticket saved = service.crear(entity);
        log.info("Ticket creado exitosamente. ID: {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Actualizar registro", description = "Actualiza completamente un registro existente")
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> actualizar(@PathVariable Long id, @Valid @RequestBody TicketDTO dto) {
        log.info("Petición PUT para actualizar ticket con ID: {}", id);
        Ticket entity = new Ticket();
        entity.setOrderId(dto.getOrderId());
        entity.setEventId(dto.getEventId());
        entity.setSeatId(dto.getSeatId());
        entity.setPrice(dto.getPrice());
        entity.setStatus(dto.getStatus());
        
        Ticket updated = service.actualizar(id, entity);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar registro", description = "Elimina un registro de la base de datos por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando ticket con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
