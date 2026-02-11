package semicolon.studentmonitoringapp.exceptions.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import semicolon.studentmonitoringapp.exceptions.NotFoundException;
import semicolon.studentmonitoringapp.exceptions.OtpException;
import semicolon.studentmonitoringapp.exceptions.SchoolClassDuplicateException;


import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException methodArgumentNotValidException) {

        Map<String, String> errors = new HashMap<>();
        methodArgumentNotValidException
                .getBindingResult()
                .getFieldErrors()
                .forEach((error) -> {
                    log.error("Validation failed - field: {}, rejected value: {}, message: {}",
                            error.getField(),
                            error.getRejectedValue(),
                            error.getDefaultMessage());
                    errors.put(error.getField(), error.getDefaultMessage());
                } );

        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(errors);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(
            NotFoundException notFoundException) {
        
        logError(notFoundException.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put("message", notFoundException.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errors);
    }

    @ExceptionHandler(SchoolClassDuplicateException.class)
    public ResponseEntity<Map<String, String>> handleSchoolClassDuplicateException(
            SchoolClassDuplicateException schoolClassDuplicateException
    ){
        logError(schoolClassDuplicateException.getMessage());// I was logging errors

        Map<String, String> errors = new HashMap<>();
        errors.put("message", schoolClassDuplicateException.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errors);

    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOtpException(
            Exception exception
    ){
        log.error("Unexpected error", exception);

        Map<String, String> errors = new HashMap<>();
        errors.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errors);
            
    }

    public ResponseEntity<Map<String, String>> handleOtpException(
            OtpException otpException
    ){
        log.error("Error occurred", otpException);

        Map<String, String> errors = new HashMap<>();
        errors.put("message", otpException.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errors);
    }








    private static void logError(String schoolClassDuplicateException) {
        log.error("message: {}", schoolClassDuplicateException);
    }
}
