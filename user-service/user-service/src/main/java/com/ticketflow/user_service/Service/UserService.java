package com.ticketflow.user_service.Service;

import com.ticketflow.user_service.Model.User;
import com.ticketflow.user_service.Repository.UserRepository;
import com.ticketflow.user_service.Exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> buscarTodos() {
        log.info("Buscando todos los usuarios en la base de datos");
        return repository.findAll();
    }

    public User buscarPorId(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con ID " + id + " no existe."));
    }

    @Transactional
    public User crear(User entity) {
        log.info("Creando nuevo usuario con username: {}", entity.getUsername());
        return repository.save(entity);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. El usuario con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }
}
