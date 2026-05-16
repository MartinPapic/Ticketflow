package com.ticketflow.access_service.Service;

import com.ticketflow.access_service.Model.Access;
import com.ticketflow.access_service.Repository.AccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessService {

    @Autowired
    private AccessRepository repository;

    public List<Access> buscarTodos() {
        return repository.findAll();
    }

    public Access buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Access crear(Access entity) {
        return repository.save(entity);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
