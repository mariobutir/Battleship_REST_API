package mario.battleship.exceptions;

public class UserOrGameDoesNotExistException extends RuntimeException {

    public UserOrGameDoesNotExistException() {
    }

    public UserOrGameDoesNotExistException(String var1) {
        super(var1);
    }

    public UserOrGameDoesNotExistException(String var1, Throwable var2) {
        super(var1, var2);
    }

    public UserOrGameDoesNotExistException(Throwable var1) {
        super(var1);
    }
}
