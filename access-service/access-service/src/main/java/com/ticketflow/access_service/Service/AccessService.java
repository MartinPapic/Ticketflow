package com.ticketflow.access_service.Service;

import com.ticketflow.access_service.Exception.ResourceNotFoundException;
import com.ticketflow.access_service.Exception.BusinessValidationException;

import org.springframework.transaction.annotation.Transactional;

import com.ticketflow.access_service.Client.TicketClient;
import com.ticketflow.access_service.Model.Access;
import com.ticketflow.access_service.Repository.AccessRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class AccessService {

    @Autowired
    private AccessRepository repository;

    @Autowired
    private TicketClient ticketClient;

    public List<Access> buscarTodos() {
        log.info("Buscando todos los registros de Access");
        return repository.findAll();
    }

    public Access buscarPorId(Long id) {
        log.info("Buscando Access con ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El registro de Access con ID " + id + " no existe."));
    }

    @Transactional
    public Access crear(Access entity) {
        log.info("Creando nuevo registro de Access");
        return repository.save(entity);
    } catch (Exception e) {
            log.error("Error al validar ticket con ticket-service: {}", e.getMessage());
            throw new RuntimeException("Error de validación de acceso: " + e.getMessage());
        }
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando Access con ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. El registro de Access con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }
}
