package com.example.studentsmanagement.Exception;

import com.example.studentsmanagement.Model.Response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ApiResponse<Map<String, String>> response =
                new ApiResponse<>(
                        false,
                        400,
                        "Validation failed",
                        errors
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidJson(
            HttpMessageNotReadableException exception) {

        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(
                        400,
                        "Invalid request body. ID must be a number"
                ));
    }

    @ExceptionHandler(TelegramChatNotFoundException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleTelegramChatNotFound(TelegramChatNotFoundException exception) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("errorCode", exception.getErrorCode());
        data.put("description", exception.getDescription());

        ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>(false , 400, exception.getMessage(), data);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(apiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(500, exception.getMessage()));
    }
}
