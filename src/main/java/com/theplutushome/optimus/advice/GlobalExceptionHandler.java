package com.theplutushome.optimus.advice;

import com.theplutushome.optimus.entity.model.ErrorResponse;
import com.theplutushome.optimus.exceptions.EmptyCollectionExceptiton;
import com.theplutushome.optimus.exceptions.NoBalanceToRedeem;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle validation errors globally
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        // Safely map validation errors
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        // Build the error response
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                "Validation failed. Please check the input.",
                errors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleInvalidJson(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("status", "400 BAD_REQUEST");
        error.put("message", "Malformed JSON request. Please check the syntax.");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.name() ,ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, String>> handleNullPointerException(NullPointerException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("status", HttpStatus.BAD_REQUEST.name());
        error.put("message", "A required parameter was null.");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("status", HttpStatus.CONFLICT.name());
        error.put("message", "A user with this username or email already exists.");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoBalanceToRedeem.class)
    public ResponseEntity<Map<String, String>> handleNoBalanceToRedeem(NoBalanceToRedeem ex) {
        Map<String, String> error = new HashMap<>();
        error.put("status", HttpStatus.NOT_ACCEPTABLE.name());
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("status", HttpStatus.NOT_FOUND.name());
        error.put("message", "User does not exist.");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotFoundException(OrderNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("status", HttpStatus.NOT_FOUND.name());
        error.put("message", "Order does not exist.");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyCollectionExceptiton.class)
    public ResponseEntity<Map<String, String>> handleEmptyCollection(EmptyCollectionExceptiton ex) {
        Map<String, String> error = new HashMap<>();
        error.put("status", HttpStatus.NO_CONTENT.name());
        error.put("message", "This collection is empty.");
        return new ResponseEntity<>(error, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidLoginCredentials(BadCredentialsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("status", HttpStatus.BAD_REQUEST.name());
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        List<Map<String, String>> errorList = new ArrayList<>();

        String message = "A database integrity violation occurred. Please check the provided data.";
        String field = "Unknown Field";
        String constraintName = ex.getMessage();

        if (constraintName.contains("users_tbl_email_key")) {
            field = "email";
            message = "The email address is already in use.";
            addErrorToList(errorList, field, message);
        }

        if (constraintName.contains("users_tbl_username_key")) {
            field = "username";
            message = "The username is already taken.";
            addErrorToList(errorList, field, message);
        }

        if (constraintName.contains("users_tbl_phone_key")) {
            field = "phone";
            message = "The phone is already taken.";
            addErrorToList(errorList, field, message);
        }

        // Final response object
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.CONFLICT.name());
        response.put("errorMessages", errorList);

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    private void addErrorToList(List<Map<String, String>> errorList, String field, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("field", field);
        error.put("message", message);
        errorList.add(error);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired at: " + ex.getClaims().getExpiration());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, String>> handleJwtException(JwtException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("status", HttpStatus.BAD_REQUEST.name());
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}

