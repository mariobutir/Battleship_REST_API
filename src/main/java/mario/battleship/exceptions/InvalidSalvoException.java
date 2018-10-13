package mario.battleship.exceptions;

public class InvalidSalvoException extends RuntimeException {

    public InvalidSalvoException() {
    }

    public InvalidSalvoException(String var1) {
        super(var1);
    }

    public InvalidSalvoException(String var1, Throwable var2) {
        super(var1, var2);
    }

    public InvalidSalvoException(Throwable var1) {
        super(var1);
    }
}
