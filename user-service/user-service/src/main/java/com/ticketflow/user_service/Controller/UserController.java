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

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService service;

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

    @GetMapping("/{id}")
    public ResponseEntity<User> buscarPorId(@PathVariable Long id) {
        log.info("Petición GET para buscar usuario con ID: {}", id);
        User entity = service.buscarPorId(id);
        return ResponseEntity.ok(entity);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Petición DELETE para eliminar usuario con ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
