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
        log.info("Validando ticket ID: {} antes de registrar acceso", entity.getTicketId());
        try {
            Object ticket = ticketClient.buscarPorId(entity.getTicketId());
            if (ticket == null) {
                log.error("Ticket ID: {} no encontrado. Acceso denegado.", entity.getTicketId());
                throw new ResourceNotFoundException("El ticket asociado ID " + entity.getTicketId() + " no existe.");
            }
            log.info("Ticket validado correctamente. Guardando registro de acceso.");
            return repository.save(entity);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al validar ticket con ticket-service: {}", e.getMessage());
            throw new BusinessValidationException("Error de validación de acceso: " + e.getMessage());
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

    @Transactional
    public Access actualizar(Long id, Access entity) {
        log.info("Actualizando Access con ID: {}", id);
        Access existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El registro de Access con ID " + id + " no existe."));
        
        try {
            Object ticket = ticketClient.buscarPorId(entity.getTicketId());
            if (ticket == null) {
                log.error("Ticket ID: {} no encontrado. Acceso denegado.", entity.getTicketId());
                throw new ResourceNotFoundException("El ticket asociado ID " + entity.getTicketId() + " no existe.");
            }
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al validar ticket con ticket-service: {}", e.getMessage());
            throw new BusinessValidationException("Error de validación de acceso: " + e.getMessage());
        }

        existente.setTicketId(entity.getTicketId());
        existente.setGate(entity.getGate());
        existente.setAccessTime(entity.getAccessTime());
        existente.setStatus(entity.getStatus());
        
        return repository.save(existente);
    }
}
