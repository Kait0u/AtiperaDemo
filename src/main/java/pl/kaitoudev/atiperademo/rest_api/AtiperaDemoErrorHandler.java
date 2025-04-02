package pl.kaitoudev.atiperademo.rest_api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import pl.kaitoudev.atiperademo.rest_api.dto.atipera_api.AtiperaErrorDTO;
import pl.kaitoudev.atiperademo.service.AtiperaDemoService;

import java.util.Map;
import java.util.Objects;

/**
 * Global exception handler for the Atipera Demo application.
 * Provides centralized error handling and standardized error responses.
 */
@RestControllerAdvice
@AllArgsConstructor
public class AtiperaDemoErrorHandler {
    /**
     * Fallback error message used when no specific message is available.
     */
    @SuppressWarnings("unused")
    private static final String FALLBACK_MESSAGE = "Something went wrong";

    private final AtiperaDemoService atiperaDemoService;

    /**
     * Handles HTTP 400 Bad Request exceptions.
     * Extracts the error message from the exception response.
     *
     * @param ex The BadRequest exception thrown by the application
     * @return ResponseEntity containing standardized error DTO with status 400
     */
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<AtiperaErrorDTO> handle400(HttpClientErrorException.BadRequest ex) {
        var responseBody = ex.getResponseBodyAs(Map.class);
        return ResponseEntity.status(400).body(
                atiperaDemoService.createErrorResponse(
                        400,
                        Objects.requireNonNull(responseBody).get("message").toString()
                )
        );
    }

    /**
     * Handles HTTP 404 Not Found exceptions.
     * Extracts the error message from the exception response.
     *
     * @param ex The NotFound exception thrown by the application
     * @return ResponseEntity containing standardized error DTO with status 404
     */
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<AtiperaErrorDTO> handle404(HttpClientErrorException.NotFound ex) {
        var responseBody = ex.getResponseBodyAs(Map.class);
        return ResponseEntity.status(404).body(
                atiperaDemoService.createErrorResponse(
                        404,
                        Objects.requireNonNull(responseBody).get("message").toString()
                )
        );
    }

    /**
     * Handles all other unexpected exceptions (HTTP 500 Internal Server Error).
     * Uses the exception's message or fallback message if not available.
     *
     * @param e The unexpected exception thrown by the application
     * @return ResponseEntity containing standardized error DTO with status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AtiperaErrorDTO> handle500(Exception e) {
        return ResponseEntity.status(500).body(
                atiperaDemoService.createErrorResponse(500, e.getMessage())
        );
    }
}