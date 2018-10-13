package mario.battleship.exceptions;

public class DoesNotExistException extends RuntimeException {

    public DoesNotExistException() {
    }

    public DoesNotExistException(String var1) {
        super(var1);
    }

    public DoesNotExistException(String var1, Throwable var2) {
        super(var1, var2);
    }

    public DoesNotExistException(Throwable var1) {
        super(var1);
    }
}