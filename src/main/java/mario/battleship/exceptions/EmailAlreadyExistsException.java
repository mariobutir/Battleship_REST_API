package mario.battleship.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
    }

    public EmailAlreadyExistsException(String var1) {
        super(var1);
    }

    public EmailAlreadyExistsException(String var1, Throwable var2) {
        super(var1, var2);
    }

    public EmailAlreadyExistsException(Throwable var1) {
        super(var1);
    }
}