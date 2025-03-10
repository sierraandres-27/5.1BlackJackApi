package cat.itacademy.s05.t01.n01.BlackJack.services;

import cat.itacademy.s05.t01.n01.BlackJack.Repository.PlayerRepository;
import cat.itacademy.s05.t01.n01.BlackJack.enums.PlayerType;
import cat.itacademy.s05.t01.n01.BlackJack.enums.Rank;
import cat.itacademy.s05.t01.n01.BlackJack.exceptions.PlayerNotFoundException;
import cat.itacademy.s05.t01.n01.BlackJack.models.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

public Mono<Player> createDealer (){

    return Mono.just(
            Player.builder()
            .name("dealer")
            .player_type(PlayerType.DEALER)
            .build()
    );
}

public Mono<Player> createPlayer (String name){

    Player player = Player.builder()
            .name(name)
            .player_type(PlayerType.PLAYER)
            .build();
    return playerRepository.save(player);

}

public Mono<Player> updateNamePlayer (int id, String name ){

    return playerRepository.findById(id)
            .flatMap(player -> {
                player.setName(name);
                return playerRepository.save(player);
            })
            .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player not found it trying update the player name")));

}

public Flux<Player>getRanking (){

    return playerRepository.findAll()
            .switchIfEmpty(Mono.error(new RuntimeException("Ranking players not found")))
            .sort((p1,p2)->Integer.compare(p2.getGames_won(),p1.getGames_won()));

}

    public Mono<Player> findPlayerById(int id) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Error finding the player by id, Player not found with ID: " + id)));
    }

    public Mono<Player> updatePlayer(Player player) {
        return playerRepository.save(player)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Error updating the player, Unable to update player")));
    }




}
