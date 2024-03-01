package com.gabriel.araovos.resources.excepitions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.gabriel.araovos.services.exceptions.DataIntegrityViolationException;
import com.gabriel.araovos.services.exceptions.ObjectNotFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionhandler {
    
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> ObjectNotFundException(ObjectNotFoundException ex, HttpServletRequest request) {
    
    StandardError error = new StandardError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(),"objeto nao encontrao!", ex.getMessage(),request.getRequestURI());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> dataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
    
    StandardError error = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),"Violacao de dados!", ex.getMessage(),request.getRequestURI());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
    
        ValidationError errors = new ValidationError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "erro de validacao!", "Erro na validacao dos campos", request.getRequestURI());

        for(FieldError x : ex.getBindingResult().getFieldErrors()) {
            errors.addError(x.getField(), x.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
