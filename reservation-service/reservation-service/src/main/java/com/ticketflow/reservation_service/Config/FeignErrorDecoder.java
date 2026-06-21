package com.ticketflow.reservation_service.Config;

import com.ticketflow.reservation_service.Exception.ResourceNotFoundException;
import com.ticketflow.reservation_service.Exception.BusinessValidationException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new ResourceNotFoundException("Recurso no encontrado en servicio remoto");
        } else if (response.status() >= 400 && response.status() <= 499) {
            return new BusinessValidationException("Error de validación en servicio remoto");
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
