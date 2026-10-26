package com.biblioteca.backend.tratamentoError;


import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

@ControllerAdvice
public class TratamentoExcecaoGlobal {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRunTimeException(RuntimeException ex){
        if (NestedExceptionUtils.getMostSpecificCause(ex) instanceof SQLException sql && sql.getErrorCode() >= 50000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sql.getMessage());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
