package com.ticketflow.user_service.Service;

import com.ticketflow.user_service.Model.User;
import com.ticketflow.user_service.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> buscarTodos() {
        return repository.findAll();
    }

    public User buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public User crear(User entity) {
        return repository.save(entity);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
