package com.ticketflow.user_service.Controller;

import com.ticketflow.user_service.DTO.UserDTO;
import com.ticketflow.user_service.Model.User;
import com.ticketflow.user_service.Service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
@Tag(name = "User", description = "API for User operations")
public class UserController {

    @Autowired
    private UserService service;

    @Operation(summary = "Get all entities", description = "Retrieves a list of all entities")
    @GetMapping
    public ResponseEntity<List<User>> buscarTodos() {
        log.info("Petición GET para listar todos los usuarios");
        List<User> lista = service.buscarTodos();
        if(lista.isEmpty()){
            log.warn("No se encontraron usuarios en el catálogo");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Get entity by ID", description = "Retrieves an entity by its identifier")
    @GetMapping("/{id}")
    public ResponseEntity<User> buscarPorId(@PathVariable Long id) {
        log.info("Petición GET para buscar usuario con ID: {}", id);
        User entity = service.buscarPorId(id);
        return ResponseEntity.ok(entity);
    }

    @Operation(summary = "Create entity", description = "Creates a new entity in the system")
    @PostMapping
    public ResponseEntity<User> crear(@Valid @RequestBody UserDTO dto) {
        log.info("Petición POST para crear usuario con username: {}", dto.getUsername());
        User entity = new User();
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setRole(dto.getRole() != null ? dto.getRole() : "CLIENT");
        entity.setActive(dto.getActive() != null ? dto.getActive() : true);
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        User saved = service.crear(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Update entity", description = "Updates an existing entity by its identifier")
    @PutMapping("/{id}")
    public ResponseEntity<User> actualizar(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        log.info("Petición PUT para actualizar usuario con ID: {}", id);
        User entity = new User();
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setRole(dto.getRole() != null ? dto.getRole() : "CLIENT");
        entity.setActive(dto.getActive() != null ? dto.getActive() : true);
        
        User updated = service.actualizar(id, entity);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete entity", description = "Deletes an entity by its identifier")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Petición DELETE para eliminar usuario con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
