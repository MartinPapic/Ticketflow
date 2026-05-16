package com.ticketflow.access_service.Service;

import com.ticketflow.access_service.Client.TicketClient;
import com.ticketflow.access_service.Model.Access;
import com.ticketflow.access_service.Repository.AccessRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AccessService {

    @Autowired
    private AccessRepository repository;

    @Autowired
    private TicketClient ticketClient;

    public List<Access> buscarTodos() {
        return repository.findAll();
    }

    public Access buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Access crear(Access entity) {
        log.info("Validando ticket ID: {} para registro de acceso", entity.getTicketId());
        
        try {
            Object ticket = ticketClient.buscarPorId(entity.getTicketId());
            if (ticket == null) {
                log.error("Ticket ID: {} no existe o es inválido.", entity.getTicketId());
                throw new RuntimeException("Ticket inválido");
            }
            log.info("Ticket validado. Registrando acceso en puerta: {}", entity.getGate());
            return repository.save(entity);
        } catch (Exception e) {
            log.error("Error al validar ticket con ticket-service: {}", e.getMessage());
            throw new RuntimeException("Error de validación de acceso: " + e.getMessage());
        }
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
