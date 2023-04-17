package ru.practicum.shareit.advice;

import org.springframework.dao.*;
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
        } else if (ex instanceof DataAccessResourceFailureException) {
            // обработка исключения, если не удалось получить доступ к ресурсу
        } else if (ex instanceof DeadlockLoserDataAccessException) {
            // обработка исключения, если была обнаружена блокировка
        } else if (ex instanceof EmptyResultDataAccessException) {
            // обработка исключения, если не были получены данные из базы
        } else if (ex instanceof IncorrectResultSizeDataAccessException) {
            // обработка исключения, если результат запроса имеет неверный размер
        } else if (ex instanceof InvalidDataAccessApiUsageException) {
            // обработка исключения, если была передана неверная аргументация в методы доступа к данным
        } else if (ex instanceof OptimisticLockingFailureException) {
            // обработка исключения, если была обнаружена оптимистическая блокировка
        } else if (ex instanceof PermissionDeniedDataAccessException) {
            // обработка исключения, если был отказан в доступе к данным
        } else if (ex instanceof QueryTimeoutException) {
            // обработка исключения, если запрос не был выполнен за отведенное время
        } else if (ex instanceof TransientDataAccessResourceException) {
            // обработка исключения, если возникли временные проблемы при доступе к ресурсу
        } else if (ex instanceof TypeMismatchDataAccessException) {
            // обработка исключения, если типы данных не совпадают
        } else {
            // обработка других исключений доступа к данным
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
        }
        return new ResponseEntity<>(errorMap, HttpStatus.resolve(httpCode));
    }
}
