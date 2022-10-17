package pl.stormit.tinyurl.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public final ResponseEntity<Object> handleMethodArgumentNotValidException(ApiException ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, Object> map = new HashMap<>();
        map.put("error", e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.CONFLICT);
    }
}