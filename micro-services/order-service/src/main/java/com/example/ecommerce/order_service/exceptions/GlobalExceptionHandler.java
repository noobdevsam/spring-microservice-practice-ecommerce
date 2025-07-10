package com.example.ecommerce.order_service.exceptions;

import com.example.ecommerce.order_service.models.ErrorResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * Global exception handler for the order service.
 * Provides centralized handling of exceptions across the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions of type EntityNotFoundException.
     * Returns a 404 Not Found response with the exception message.
     *
     * @param ex The EntityNotFoundException instance.
     * @return A ResponseEntity containing the exception message.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    /**
     * Handles exceptions of type MethodArgumentNotValidException.
     * Extracts validation errors and returns a 400 Bad Request response with the error details.
     *
     * @param ex The MethodArgumentNotValidException instance.
     * @return A ResponseEntity containing an ErrorResponseDTO with validation error details.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();

        ex.getBindingResult().getAllErrors()
                .forEach(error -> {
                    if (error instanceof FieldError) {
                        var fieldName = ((FieldError) error).getField();
                        var errorMessage = error.getDefaultMessage();
                        errors.put(fieldName, errorMessage);
                    }
                });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(errors));
    }

    /**
     * Handles exceptions of type BusinessException.
     * Returns a 400 Bad Request response with the exception message.
     *
     * @param ex The BusinessException instance.
     * @return A ResponseEntity containing the exception message.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

}