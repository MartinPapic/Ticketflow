package com.ticketflow.reservation_service.Controller;

import org.springframework.http.HttpStatus;

import com.ticketflow.reservation_service.DTO.ReservationDTO;
import com.ticketflow.reservation_service.Model.Reservation;
import com.ticketflow.reservation_service.Service.ReservationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservation")
@Slf4j
@Tag(name = "Reservation", description = "API for Reservation operations")
public class ReservationController {

    @Autowired
    private ReservationService service;

    @Operation(summary = "Get all entities", description = "Retrieves a list of all entities")
    @GetMapping
    public ResponseEntity<List<Reservation>> buscarTodos() {
        log.info("Petición GET para listar todos los Reservations");
        List<Reservation> lista = service.buscarTodos();
        if (lista.isEmpty()) {
            log.warn("No se encontraron registros de Reservation");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Get entity by ID", description = "Retrieves an entity by its identifier")
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> buscarPorId(@PathVariable Long id) {
        log.info("Petición GET para obtener Reservation con ID: {}", id);
        Reservation entity = service.buscarPorId(id);
        return ResponseEntity.ok(entity);
    }

    @Operation(summary = "Create entity", description = "Creates a new entity in the system")
    @PostMapping
    public ResponseEntity<Reservation> crear(@Valid @RequestBody ReservationDTO dto) {
        log.info("Creando reserva para usuario ID: {} en evento ID: {}", dto.getUserId(), dto.getEventId());
        Reservation entity = new Reservation();
        entity.setUserId(dto.getUserId());
        entity.setEventId(dto.getEventId());
        entity.setSeatId(dto.getSeatId());
        entity.setStatus(dto.getStatus());
        entity.setExpirationTime(dto.getExpirationTime());
        
        Reservation saved = service.crear(entity);
        log.info("Reservación creada con éxito. ID: {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Update entity", description = "Updates an existing entity by its identifier")
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> actualizar(@PathVariable Long id, @Valid @RequestBody ReservationDTO dto) {
        log.info("Petición PUT para actualizar Reservation con ID: {}", id);
        Reservation entity = new Reservation();
        entity.setUserId(dto.getUserId());
        entity.setEventId(dto.getEventId());
        entity.setSeatId(dto.getSeatId());
        entity.setStatus(dto.getStatus());
        entity.setExpirationTime(dto.getExpirationTime());
        
        Reservation updated = service.actualizar(id, entity);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete entity", description = "Deletes an entity by its identifier")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Petición DELETE para eliminar Reservation con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
