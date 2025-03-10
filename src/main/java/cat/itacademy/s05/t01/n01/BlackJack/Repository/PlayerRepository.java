package cat.itacademy.s05.t01.n01.BlackJack.Repository;

import cat.itacademy.s05.t01.n01.BlackJack.models.Player;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PlayerRepository extends ReactiveCrudRepository<Player,Integer> {



}
