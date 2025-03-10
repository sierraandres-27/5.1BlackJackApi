package cat.itacademy.s05.t01.n01.BlackJack.Repository;

import cat.itacademy.s05.t01.n01.BlackJack.models.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface GameRepository extends ReactiveMongoRepository<Game,String> {


}
