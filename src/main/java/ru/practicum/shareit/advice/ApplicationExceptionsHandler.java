package ru.practicum.shareit.advice;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionsHandler {
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, String>> handleEmptyResultDataAccessException(DataAccessException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        int statusCode = 404;
        if (ex instanceof DataIntegrityViolationException) {
            // обработка исключения, если было нарушение целостности данных
            statusCode = 409;
        }
        return new ResponseEntity<>(errorMap, HttpStatus.valueOf(statusCode));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage());
        return new ResponseEntity<>(errorMap, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestException(BadRequestException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage());
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, String>> handleConflictException(ConflictException e) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", e.getMessage());
        return new ResponseEntity<>(errorMap, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleInvalidArgument(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        String objectName = e.getBindingResult().getObjectName();
        int httpCode = 404;
        switch (objectName) {
            case "userDto":
                if (errorMap.containsKey("email")) {
                    httpCode = 400;
                }
                break;
            case "itemDto":
                if (errorMap.containsKey("name") || errorMap.containsKey("description") ||
                        errorMap.containsKey("available")) {
                    httpCode = 400;
                }
                break;
            case "bookingDto":
                if (errorMap.containsKey("start") || errorMap.containsKey("end")) {
                    httpCode = 400;
                }
                break;
            case "commentDto":
            case "itemRequestDto":
                httpCode = 400;
                break;
        }
        return new ResponseEntity<>(errorMap, HttpStatus.resolve(httpCode));
    }
}
