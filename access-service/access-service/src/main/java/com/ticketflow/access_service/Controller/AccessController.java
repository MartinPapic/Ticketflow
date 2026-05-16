package com.ticketflow.access_service.Controller;

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
        log.info("Buscando todos los registros de acceso");
        List<Access> lista = service.buscarTodos();
        if(lista.isEmpty()){
            log.warn("No se encontraron registros de acceso");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Access> buscarPorId(@PathVariable Long id) {
        log.info("Buscando registro de acceso con ID: {}", id);
        Access entity = service.buscarPorId(id);
        if (entity == null) {
            log.error("Registro de acceso con ID: {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando registro de acceso con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
