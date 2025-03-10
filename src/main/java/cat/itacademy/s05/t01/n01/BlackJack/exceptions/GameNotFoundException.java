package cat.itacademy.s05.t01.n01.BlackJack.exceptions;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String message) {

        super(message);
    }
}
