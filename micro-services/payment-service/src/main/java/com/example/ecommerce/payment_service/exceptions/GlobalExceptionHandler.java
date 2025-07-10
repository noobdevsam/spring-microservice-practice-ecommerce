package com.example.ecommerce.payment_service.exceptions;

import com.example.ecommerce.payment_service.models.ErrorResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    /**
     * Handles exceptions of type MethodArgumentNotValidException.
     * This method processes validation errors and constructs an error response.
     *
     * @param ex the MethodArgumentNotValidException containing validation errors
     * @return a ResponseEntity containing an ErrorResponseDTO with validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex
    ) {
        // A map to store field names and their corresponding error messages
        var errors = new HashMap<String, String>();

        // Iterate through all validation errors and extract field names and error messages
        ex.getBindingResult().getAllErrors()
                .forEach(error -> {
                    if (error instanceof FieldError) {
                        var fieldName = ((FieldError) error).getField();
                        var errorMessage = error.getDefaultMessage();
                        errors.put(fieldName, errorMessage);
                    }
                });

        // Return a BAD_REQUEST response with the error details encapsulated in ErrorResponseDTO
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(errors));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

}
