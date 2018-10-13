package mario.battleship.exceptions;

public class NotYourTurnException extends RuntimeException {

    public NotYourTurnException() {
    }

    public NotYourTurnException(String var1) {
        super(var1);
    }

    public NotYourTurnException(String var1, Throwable var2) {
        super(var1, var2);
    }

    public NotYourTurnException(Throwable var1) {
        super(var1);
    }
}
