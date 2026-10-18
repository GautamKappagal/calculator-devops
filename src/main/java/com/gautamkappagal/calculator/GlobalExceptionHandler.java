package com.gautamkappagal.calculator;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice   // applies to all controllers; return values written as JSON
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissing(MissingServletRequestParameterException ex,
                                                  HttpServletRequest req) {
        return badRequest("Missing required parameter: " + ex.getParameterName(), req);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)   // e.g. a=abc -> BigDecimal fails
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                       HttpServletRequest req) {
        return badRequest("Parameter '" + ex.getName() + "' must be a valid number, but was '"
                + ex.getValue() + "'", req);
    }

    @ExceptionHandler(DivisionByZeroException.class)
    public ResponseEntity<ApiError> handleDivisionByZero(DivisionByZeroException ex,
                                                         HttpServletRequest req) {
        return badRequest(ex.getMessage(), req);
    }

    private ResponseEntity<ApiError> badRequest(String message, HttpServletRequest req) {
        ApiError body = new ApiError(Instant.now().toString(), 400, "Bad Request",
                message, req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}