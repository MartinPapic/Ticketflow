package com.ticketflow.payment_service;

import com.ticketflow.payment_service.Model.Payment;
import com.ticketflow.payment_service.Repository.PaymentRepository;
import com.ticketflow.payment_service.Service.PaymentService;
import com.ticketflow.payment_service.Exception.ResourceNotFoundException;
import com.ticketflow.payment_service.Exception.BusinessValidationException;
import com.ticketflow.payment_service.Client.OrderClient;
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
public class PaymentServiceTest {

    @Mock
    private PaymentRepository repository;

    @Mock
    private OrderClient orderClient;

    @InjectMocks
    private PaymentService service;

    private Payment mockEntity;

    @BeforeEach
    void setUp() {
        mockEntity = new Payment();
        mockEntity.setId(1L);
        mockEntity.setOrderId(100L);
        mockEntity.setAmount(150.00);
        mockEntity.setPaymentMethod("CREDIT_CARD");
        mockEntity.setPaymentDate(LocalDateTime.now().toString());
        mockEntity.setStatus("SUCCESS");
    }

    @Test
    void givenList_whenBuscarTodos_thenReturnList() {
        // Given
        when(repository.findAll()).thenReturn(Arrays.asList(mockEntity));

        // When
        List<Payment> result = service.buscarTodos();

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
        Payment result = service.buscarPorId(1L);

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
    void givenValidOrderId_whenCrear_thenReturnSavedEntity() {
        // Given
        when(orderClient.buscarPorId(100L)).thenReturn(new Object());
        when(repository.save(any(Payment.class))).thenReturn(mockEntity);

        // When
        Payment result = service.crear(mockEntity);

        // Then
        assertNotNull(result);
        assertEquals(150.00, result.getAmount());
        verify(orderClient, times(1)).buscarPorId(100L);
        verify(repository, times(1)).save(any(Payment.class));
    }

    @Test
    void givenInvalidOrderId_whenCrear_thenThrowResourceNotFoundException() {
        // Given
        when(orderClient.buscarPorId(100L)).thenReturn(null);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            service.crear(mockEntity);
        });
        verify(orderClient, times(1)).buscarPorId(100L);
        verify(repository, never()).save(any(Payment.class));
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
        when(orderClient.buscarPorId(100L)).thenReturn(new Object());
        when(repository.save(any(Payment.class))).thenReturn(mockEntity);
        
        Payment updateData = new Payment();
        updateData.setOrderId(100L);
        updateData.setAmount(200.00);

        // When
        Payment result = service.actualizar(1L, updateData);

        // Then
        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
        verify(orderClient, times(1)).buscarPorId(100L);
        verify(repository, times(1)).save(any(Payment.class));
    }
}
