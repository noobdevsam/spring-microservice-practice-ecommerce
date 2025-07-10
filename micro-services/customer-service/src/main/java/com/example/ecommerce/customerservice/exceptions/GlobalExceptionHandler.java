package com.example.ecommerce.customerservice.exceptions;

import com.example.ecommerce.customerservice.model.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * GlobalExceptionHandler is a centralized exception handling class for the application.
 * It uses @RestControllerAdvice to handle exceptions globally across all controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles CustomerNotFoundException.
     *
     * @param ex the exception thrown when a customer is not found
     * @return a ResponseEntity containing the error message and HTTP status NOT_FOUND
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    /**
     * Handles MethodArgumentNotValidException.
     * This exception is thrown when validation on an argument annotated with @Valid fails.
     *
     * @param ex the exception containing validation errors
     * @return a ResponseEntity containing a map of field names to error messages and HTTP status BAD_REQUEST
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();

        // Extract validation errors and map field names to error messages
        ex.getBindingResult().getAllErrors()
                .forEach(error -> {
                    if (error instanceof FieldError) {
                        var fieldName = ((FieldError) error).getField();
                        var errorMessage = error.getDefaultMessage();
                        errors.put(fieldName, errorMessage);
                    }
                });

        // Return the error response with BAD_REQUEST status
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(errors));
    }
}