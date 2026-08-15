package com.example.demo.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  void handleNotFound_withResourceNotFoundException_returns404() {
    ResponseEntity<ApiError> response =
        handler.handleNotFound(new ResourceNotFoundException("Student not found"));

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Student not found", response.getBody().message());
  }

  @Test
  void handleNotFound_withEntityNotFoundException_returns404() {
    ResponseEntity<ApiError> response =
        handler.handleNotFound(new EntityNotFoundException("Entity missing"));

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void handleForbidden_withForbiddenOperationException_returns403() {
    ResponseEntity<ApiError> response =
        handler.handleForbidden(new ForbiddenOperationException("Not your record"));

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertEquals("Not your record", response.getBody().message());
  }

  @Test
  void handleForbidden_withAccessDeniedException_returns403() {
    ResponseEntity<ApiError> response =
        handler.handleForbidden(new AccessDeniedException("Denied"));

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void handleAuthenticationFailure_returns401WithGenericMessage() {
    ResponseEntity<ApiError> response =
        handler.handleAuthenticationFailure(new BadCredentialsException("wrong password"));

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("Email ou mot de passe incorrect", response.getBody().message());
  }

  @Test
  void handleDataIntegrityViolation_returns409() {
    ResponseEntity<ApiError> response =
        handler.handleDataIntegrityViolation(new DataIntegrityViolationException("duplicate key"));

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  void handleIllegalArgument_returns400WithMessage() {
    ResponseEntity<ApiError> response =
        handler.handleIllegalArgument(new IllegalArgumentException("Email already used"));

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Email already used", response.getBody().message());
  }

  @Test
  void handleGeneric_returns500WithGenericMessage() {
    ResponseEntity<ApiError> response = handler.handleGeneric(new RuntimeException("boom"));

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNotNull(response.getBody());
  }
}
