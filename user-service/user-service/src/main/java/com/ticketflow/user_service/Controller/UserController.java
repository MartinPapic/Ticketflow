package com.ticketflow.user_service.Controller;

import com.ticketflow.user_service.DTO.UserDTO;
import com.ticketflow.user_service.Model.User;
import com.ticketflow.user_service.Service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<List<User>> buscarTodos() {
        log.info("Buscando todos los usuarios");
        List<User> lista = service.buscarTodos();
        if(lista.isEmpty()){
            log.warn("No se encontraron usuarios");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> buscarPorId(@PathVariable Long id) {
        log.info("Buscando usuario con ID: {}", id);
        User entity = service.buscarPorId(id);
        if (entity == null) {
            log.error("Usuario con ID: {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<User> crear(@Valid @RequestBody UserDTO dto) {
        log.info("Creando nuevo usuario con username: {}", dto.getUsername());
        User entity = new User();
        // Here we'd map DTO to Entity. For simplicity, just saving new entity or manual map
        // assuming standard mapper logic
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setRole(dto.getRole());
        entity.setActive(dto.getActive());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        User saved = service.crear(entity);
        log.info("Usuario creado exitosamente con ID: {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
