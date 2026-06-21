package com.ticketflow.report_service;

import com.ticketflow.report_service.Model.Report;
import com.ticketflow.report_service.Repository.ReportRepository;
import com.ticketflow.report_service.Service.ReportService;
import com.ticketflow.report_service.Exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private ReportRepository repository;

    @InjectMocks
    private ReportService service;

    private Report mockEntity;

    @BeforeEach
    void setUp() {
        mockEntity = new Report();
        mockEntity.setId(1L);
        mockEntity.setName("Monthly Sales");
        mockEntity.setType("SALES");
        mockEntity.setGeneratedAt(LocalDateTime.now().toString());
        mockEntity.setFileUrl("http://s3.aws.com/report1.pdf");
    }

    @Test
    void givenList_whenBuscarTodos_thenReturnList() {
        // Given
        when(repository.findAll()).thenReturn(Arrays.asList(mockEntity));

        // When
        List<Report> result = service.buscarTodos();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void givenValidId_whenBuscarPorId_thenReturnEntity() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(mockEntity));

        // When
        Report result = service.buscarPorId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void givenInvalidId_whenBuscarPorId_thenThrowResourceNotFoundException() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            service.buscarPorId(99L);
        });
        verify(repository, times(1)).findById(99L);
    }

    @Test
    void givenEntity_whenCrear_thenReturnSavedEntity() {
        // Given
        when(repository.save(any(Report.class))).thenReturn(mockEntity);

        // When
        Report result = service.crear(mockEntity);

        // Then
        assertNotNull(result);
        assertEquals("SALES", result.getType());
        verify(repository, times(1)).save(any(Report.class));
    }

    @Test
    void givenValidId_whenEliminar_thenDeleteEntity() {
        // Given
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // When
        service.eliminar(1L);

        // Then
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void givenInvalidId_whenEliminar_thenThrowException() {
        // Given
        when(repository.existsById(99L)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            service.eliminar(99L);
        });
        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).deleteById(99L);
    }

    @Test
    void givenValidEntity_whenActualizar_thenReturnUpdatedEntity() {
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(mockEntity));
        when(repository.save(any(Report.class))).thenReturn(mockEntity);
        
        Report updateData = new Report();
        updateData.setName("Weekly Sales");

        // When
        Report result = service.actualizar(1L, updateData);

        // Then
        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Report.class));
    }
}
