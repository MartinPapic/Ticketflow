package com.ticketflow.report_service.Service;

import com.ticketflow.report_service.Exception.ResourceNotFoundException;
import com.ticketflow.report_service.Exception.BusinessValidationException;

import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.ticketflow.report_service.Model.Report;
import com.ticketflow.report_service.Repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ReportService {

    @Autowired
    private ReportRepository repository;

    public List<Report> buscarTodos() {
        log.info("Buscando todos los registros de Report");
        return repository.findAll();
    }

    public Report buscarPorId(Long id) {
        log.info("Buscando Report con ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El registro de Report con ID " + id + " no existe."));
    }

    @Transactional
    public Report crear(Report entity) {
        log.info("Creando nuevo registro de Report");
        return repository.save(entity);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando Report con ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. El registro de Report con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }

    @Transactional
    public Report actualizar(Long id, Report entity) {
        log.info("Actualizando Report con ID: {}", id);
        Report existente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El registro de Report con ID " + id + " no existe."));
        
        existente.setName(entity.getName());
        existente.setType(entity.getType());
        existente.setGeneratedAt(entity.getGeneratedAt());
        existente.setFileUrl(entity.getFileUrl());
        
        return repository.save(existente);
    }
}
