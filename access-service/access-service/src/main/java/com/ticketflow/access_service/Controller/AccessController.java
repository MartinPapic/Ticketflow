package com.ticketflow.access_service.Controller;

import org.springframework.http.HttpStatus;

import com.ticketflow.access_service.DTO.AccessDTO;
import com.ticketflow.access_service.Model.Access;
import com.ticketflow.access_service.Service.AccessService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/access")
@Slf4j
public class AccessController {

    @Autowired
    private AccessService service;

    @GetMapping
    public ResponseEntity<List<Access>> buscarTodos() {
        log.info("Petición GET para listar todos los Accesss");
        List<Access> lista = service.buscarTodos();
        if (lista.isEmpty()) {
            log.warn("No se encontraron registros de Access");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Access> buscarPorId(@PathVariable Long id) {
        log.info("Petición GET para obtener Access con ID: {}", id);
        Access entity = service.buscarPorId(id);
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<Access> crear(@Valid @RequestBody AccessDTO dto) {
        log.info("Registrando acceso para ticket ID: {} en puerta: {}", dto.getTicketId(), dto.getGate());
        Access entity = new Access();
        entity.setTicketId(dto.getTicketId());
        entity.setGate(dto.getGate());
        entity.setAccessTime(dto.getAccessTime());
        entity.setStatus(dto.getStatus());
        
        Access saved = service.crear(entity);
        log.info("Acceso registrado exitosamente. ID: {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Access> actualizar(@PathVariable Long id, @Valid @RequestBody AccessDTO dto) {
        log.info("Petición PUT para actualizar Access con ID: {}", id);
        Access entity = new Access();
        entity.setTicketId(dto.getTicketId());
        entity.setGate(dto.getGate());
        entity.setAccessTime(dto.getAccessTime());
        entity.setStatus(dto.getStatus());
        
        Access updated = service.actualizar(id, entity);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Petición DELETE para eliminar Access con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
