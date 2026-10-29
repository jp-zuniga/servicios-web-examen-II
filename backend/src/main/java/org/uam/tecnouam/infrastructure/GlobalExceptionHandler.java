package org.uam.tecnouam.infrastructure;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import org.uam.tecnouam.application.exceptions.CodigoPatrimonialDuplicado;
import org.uam.tecnouam.application.exceptions.EquipoConPrestamosActivos;
import org.uam.tecnouam.application.exceptions.RecursoNoEncontrado;
import org.uam.tecnouam.domain.exceptions.EquipoNoDisponible;
import org.uam.tecnouam.domain.exceptions.FechaDevolucionInvalida;
import org.uam.tecnouam.domain.exceptions.PrestamoYaDevuelto;

@ControllerAdvice public class GlobalExceptionHandler {
    // 400: datos inválidos

    @ExceptionHandler(FechaDevolucionInvalida.class)
    public ResponseEntity<Map<String, String>> handleFechaDevolucionInvalida(
        FechaDevolucionInvalida ex
    ) {
        return ResponseEntity.badRequest()
            .body(Map.of("fechaDevolucionPrevista", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex
    ) {
        Map<String, String> errores = new LinkedHashMap<>();

        ex.getBindingResult()
            .getFieldErrors()
            .forEach(error -> errores.putIfAbsent(
                error.getField(),
                error.getDefaultMessage()
            ));

        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, String>> handleHandlerMethodValidation(
        HandlerMethodValidationException ex
    ) {
        Map<String, String> errores = new LinkedHashMap<>();

        ex.getParameterValidationResults()
            .forEach(result -> result.getResolvableErrors()
                .forEach(error -> errores.putIfAbsent(
                    result.getMethodParameter().getParameterName(),
                    error.getDefaultMessage()
                )));

        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex
    ) {
        return ResponseEntity.badRequest()
            .body(Map.of(
                "error",
                "Cuerpo inválido: revise el JSON, el formato de fechas (yyyy-MM-dd) "
                    + "y los valores permitidos de tipo (PORTATIL, PROYECTOR, TABLETA)."
            ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatch(
        MethodArgumentTypeMismatchException ex
    ) {
        return ResponseEntity.badRequest()
            .body(Map.of(ex.getName(), "Valor inválido: %s".formatted(ex.getValue())));
    }

    // 401: credenciales incorrectas en el login

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthentication(
        AuthenticationException ex
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "Usuario o contraseña incorrectos."));
    }

    // 404: recurso inexistente

    @ExceptionHandler(RecursoNoEncontrado.class)
    public ResponseEntity<Map<String, String>> handleRecursoNoEncontrado(
        RecursoNoEncontrado ex
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", ex.getMessage()));
    }

    // 409: conflictos de negocio

    @ExceptionHandler({
        EquipoNoDisponible.class,
        PrestamoYaDevuelto.class,
        EquipoConPrestamosActivos.class,
        CodigoPatrimonialDuplicado.class
    }) public ResponseEntity<Map<String, String>> handleConflicto(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler({
        DataIntegrityViolationException.class,
        PessimisticLockingFailureException.class
    }) public ResponseEntity<Map<String, String>> handleIntegridad(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of(
                "error",
                "La operación entra en conflicto con el estado actual de los datos."
            ));
    }
}
