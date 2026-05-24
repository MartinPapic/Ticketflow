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

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
@Slf4j
public class NotificationController {

    @Autowired
    private NotificationService service;

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

    @GetMapping("/{id}")
    public ResponseEntity<Notification> buscarPorId(@PathVariable Long id) {
        log.info("Petición GET para obtener Notification con ID: {}", id);
        Notification entity = service.buscarPorId(id);
        return ResponseEntity.ok(entity);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Petición DELETE para eliminar Notification con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
