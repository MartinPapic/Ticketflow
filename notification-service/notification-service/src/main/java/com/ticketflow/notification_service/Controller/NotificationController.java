package com.ticketflow.notification_service.Controller;

import org.springframework.http.HttpStatus;

import com.ticketflow.notification_service.DTO.NotificationDTO;
import com.ticketflow.notification_service.Model.Notification;
import com.ticketflow.notification_service.Service.NotificationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
@Slf4j
@Tag(name = "Notification", description = "API for Notification operations")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @Operation(summary = "Get all entities", description = "Retrieves a list of all entities")
    @GetMapping
    public ResponseEntity<List<Notification>> buscarTodos() {
        log.info("Petición GET para listar todos los Notifications");
        List<Notification> lista = service.buscarTodos();
        if (lista.isEmpty()) {
            log.warn("No se encontraron registros de Notification");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Get entity by ID", description = "Retrieves an entity by its identifier")
    @GetMapping("/{id}")
    public ResponseEntity<Notification> buscarPorId(@PathVariable Long id) {
        log.info("Petición GET para obtener Notification con ID: {}", id);
        Notification entity = service.buscarPorId(id);
        return ResponseEntity.ok(entity);
    }

    @Operation(summary = "Create entity", description = "Creates a new entity in the system")
    @PostMapping
    public ResponseEntity<Notification> crear(@Valid @RequestBody NotificationDTO dto) {
        log.info("Enviando notificación tipo {} al usuario ID: {}", dto.getType(), dto.getUserId());
        Notification entity = new Notification();
        entity.setUserId(dto.getUserId());
        entity.setMessage(dto.getMessage());
        entity.setType(dto.getType());
        entity.setSentAt(dto.getSentAt());
        entity.setStatus(dto.getStatus());
        
        Notification saved = service.crear(entity);
        log.info("Notificación registrada exitosamente. ID: {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Update entity", description = "Updates an existing entity by its identifier")
    @PutMapping("/{id}")
    public ResponseEntity<Notification> actualizar(@PathVariable Long id, @Valid @RequestBody NotificationDTO dto) {
        log.info("Petición PUT para actualizar Notification con ID: {}", id);
        Notification entity = new Notification();
        entity.setUserId(dto.getUserId());
        entity.setMessage(dto.getMessage());
        entity.setType(dto.getType());
        entity.setSentAt(dto.getSentAt());
        entity.setStatus(dto.getStatus());
        
        Notification updated = service.actualizar(id, entity);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete entity", description = "Deletes an entity by its identifier")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Petición DELETE para eliminar Notification con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
