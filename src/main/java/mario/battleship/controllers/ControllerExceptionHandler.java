package mario.battleship.controllers;

import mario.battleship.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> emailAlreadyExists(Exception exception, WebRequest request) {
        return new ResponseEntity<>(PlayerController.conflict, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DoesNotExistException.class)
    public ResponseEntity<Object> playerDoesNotExist(Exception exception, WebRequest request) {
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<Object> noContent(Exception exception, WebRequest request) {
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(UserOrGameDoesNotExistException.class)
    public ResponseEntity<Object> challengerOrOpponentDoesNotExist(Exception exception, WebRequest request) {
        return new ResponseEntity<>(GameController.conflict, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotYourTurnException.class)
    public ResponseEntity<Object> notPlayersTurn(Exception exception, WebRequest request) {
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(GameIsFinishedException.class)
    public ResponseEntity<Object> gameIsFinished(Exception exception, WebRequest request) {
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(InvalidSalvoException.class)
    public ResponseEntity<Object> invalidSalvo(Exception exception, WebRequest request) {
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
