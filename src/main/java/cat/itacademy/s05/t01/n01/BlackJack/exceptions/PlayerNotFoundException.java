package cat.itacademy.s05.t01.n01.BlackJack.exceptions;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(String message) {

        super(message);
    }
}
