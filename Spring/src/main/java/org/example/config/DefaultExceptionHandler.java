package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Response;
import org.example.util.AppException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleMyException(Exception ex, WebRequest request) {
        if (ex instanceof AppException) {
            return Response.fail(HttpStatus.BAD_REQUEST.value(), ex.getMessage()).toResponseEntity();
        } else {
            return Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()).toResponseEntity();
        }
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                                             @Nullable HttpHeaders headers, HttpStatusCode statusCode, @Nullable WebRequest request) {
        return Response.fail(statusCode.value(), ex.getMessage()).toResponseEntity();
    }

}