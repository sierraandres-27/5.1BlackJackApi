package cat.itacademy.s05.t01.n01.BlackJack.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

public class GlobalExceptionHandler {


    @ExceptionHandler(PlayerNotFoundException.class)
    public Mono<ResponseEntity<String>> handlePlayerNotFoundException (PlayerNotFoundException exception){

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage()));

    }


    @ExceptionHandler(GameNotFoundException.class)
    public Mono<ResponseEntity<String>> handleGameNotFoundException (GameNotFoundException exception){

    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage()));

    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleGenericException (Exception exception){

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("An expected error curred"+exception.getMessage()));

    }

}
