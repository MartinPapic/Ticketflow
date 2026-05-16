package com.ticketflow.notification_service.Service;

import com.ticketflow.notification_service.Model.Notification;
import com.ticketflow.notification_service.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repository;

    public List<Notification> buscarTodos() {
        return repository.findAll();
    }

    public Notification buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Notification crear(Notification entity) {
        return repository.save(entity);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
