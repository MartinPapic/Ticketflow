package com.ticketflow.report_service.Service;

import com.ticketflow.report_service.Model.Report;
import com.ticketflow.report_service.Repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository repository;

    public List<Report> buscarTodos() {
        return repository.findAll();
    }

    public Report buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Report crear(Report entity) {
        return repository.save(entity);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
