package ar.fuentes.ale.productapi.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptinGeralHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleVaidationExcetion(ValidationException validationException){
        var details = new ExceptionDetails();
        details.setStatus(HttpStatus.BAD_REQUEST.value());
        details.setMessagem(validationException.getMessage());
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationExcetion(AuthenticationException authenticationException){
        var details = new ExceptionDetails();
        details.setStatus(HttpStatus.UNAUTHORIZED.value());
        details.setMessagem(authenticationException.getMessage());
        return new ResponseEntity<>(details, HttpStatus.UNAUTHORIZED) ;
    }

}
