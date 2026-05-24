package com.ticketflow.notification_service.Service;

import com.ticketflow.notification_service.Exception.ResourceNotFoundException;
import com.ticketflow.notification_service.Exception.BusinessValidationException;

import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.ticketflow.notification_service.Model.Notification;
import com.ticketflow.notification_service.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class NotificationService {

    @Autowired
    private NotificationRepository repository;

    public List<Notification> buscarTodos() {
        log.info("Buscando todos los registros de Notification");
        return repository.findAll();
    }

    public Notification buscarPorId(Long id) {
        log.info("Buscando Notification con ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El registro de Notification con ID " + id + " no existe."));
    }

    @Transactional
    public Notification crear(Notification entity) {
        log.info("Creando nuevo registro de Notification");
        return repository.save(entity);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando Notification con ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. El registro de Notification con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }
}
