package com.tpi.forexapi.handler;

import com.tpi.forexapi.dto.ExceptionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {Exception.class})
    public ResponseEntity<ExceptionDTO> handleConflict(
            RuntimeException ex, WebRequest request) {
        return ResponseEntity.internalServerError().body(new ExceptionDTO(ex.getMessage()));
    }

}
