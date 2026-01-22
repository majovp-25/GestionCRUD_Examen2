package com.examen2.GestionCRUD.exception; // <--- CONFIRMA QUE ESTE SEA EL PAQUETE CORRECTO

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {

    // 1. ERROR DE VALIDACIÓN MANUAL
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), req);
    }

    // 2. ERROR DE BASE DE DATOS (Ej: SKU duplicado)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDuplicate(DataIntegrityViolationException ex, HttpServletRequest req) {
        // Esto pasa si intentas guardar un SKU que ya existe
        String mensaje = "Error de integridad en la base de datos. Posiblemente el SKU ya existe.";
        return buildResponse(HttpStatus.CONFLICT, mensaje, req);
    }

    // 3. ERROR DE FORMATO JSON (Ej: Mandar texto en el campo precio)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleBadJson(HttpMessageNotReadableException ex, HttpServletRequest req) {
        String mensaje = "Formato de JSON inválido. Verifica que los números no tengan comillas o letras.";
        return buildResponse(HttpStatus.BAD_REQUEST, mensaje, req);
    }

    // 4. ERROR EN LA URL (Ej: Mandar letras en vez de ID numérico: /products/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleBadUrlParam(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String mensaje = "El parámetro '" + ex.getName() + "' debe ser del tipo correcto (ej: número para ID).";
        return buildResponse(HttpStatus.BAD_REQUEST, mensaje, req);
    }

    // 5. ERRORES GENERALES (Fallback: ID no encontrado o fallos inesperados)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(RuntimeException ex, HttpServletRequest req) {
        // Si el mensaje dice "no encontrado", devolvemos 404
        if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("no encontrado")) {
            return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), req);
        }
        // Para cualquier otro error desconocido
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor: " + ex.getMessage(), req);
    }

    // Método privado para no repetir código al construir la respuesta
    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String message, HttpServletRequest req) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                req.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }
}