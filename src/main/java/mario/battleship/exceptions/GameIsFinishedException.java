package mario.battleship.exceptions;

public class GameIsFinishedException extends RuntimeException {

    public GameIsFinishedException() {
    }

    public GameIsFinishedException(String var1) {
        super(var1);
    }

    public GameIsFinishedException(String var1, Throwable var2) {
        super(var1, var2);
    }

    public GameIsFinishedException(Throwable var1) {
        super(var1);
    }
}
