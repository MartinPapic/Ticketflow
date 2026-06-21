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

@Tag(name = "Reservation", description = "Operaciones del microservicio de Reservation")
@RestController
@RequestMapping("/api/v1/reservation")
@Slf4j
@Tag(name = "Reservation", description = "API for Reservation operations")
public class ReservationController {

    @Autowired
    private ReservationService service;

    @Operation(summary = "Obtener todos los registros", description = "Retorna una lista completa de entidades")
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

    @Operation(summary = "Buscar por ID", description = "Busca un registro específico por su identificador único")
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> buscarPorId(@PathVariable Long id) {
        log.info("Petición GET para obtener Reservation con ID: {}", id);
        Reservation entity = service.buscarPorId(id);
        return ResponseEntity.ok(entity);
    }

    @Operation(summary = "Crear nuevo registro", description = "Registra una nueva entidad en la base de datos")
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

    @Operation(summary = "Actualizar registro", description = "Actualiza completamente un registro existente")
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

    @Operation(summary = "Eliminar registro", description = "Elimina un registro de la base de datos por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Petición DELETE para eliminar Reservation con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
