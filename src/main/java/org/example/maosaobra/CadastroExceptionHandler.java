package org.example.maosaobra;

import org.example.maosaobra.controller.ClienteController;
import org.example.maosaobra.controller.TrabalhadorController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice(assignableTypes = {ClienteController.class, TrabalhadorController.class})
public class CadastroExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidacao(MethodArgumentNotValidException e) {
        String mensagem = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Dados inválidos!");

        return ResponseEntity.badRequest().body(Map.of("erro", mensagem));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleRegra(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleStatus(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(Map.of("erro", e.getReason()));
    }
}
