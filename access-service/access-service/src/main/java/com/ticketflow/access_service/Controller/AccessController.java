package com.ticketflow.access_service.Controller;

import com.ticketflow.access_service.DTO.AccessDTO;
import com.ticketflow.access_service.Model.Access;
import com.ticketflow.access_service.Service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/access")
public class AccessController {

    @Autowired
    private AccessService service;

    @GetMapping
    public ResponseEntity<List<Access>> buscarTodos() {
        List<Access> lista = service.buscarTodos();
        if(lista.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Access> buscarPorId(@PathVariable Long id) {
        Access entity = service.buscarPorId(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<Access> crear(@RequestBody AccessDTO dto) {
        Access entity = new Access();
        // Here we'd map DTO to Entity. For simplicity, just saving new entity or manual map
        // assuming standard mapper logic
        entity.setTicketId(dto.getTicketId());
        entity.setGate(dto.getGate());
        entity.setAccessTime(dto.getAccessTime());
        entity.setStatus(dto.getStatus());
        return ResponseEntity.ok(service.crear(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
