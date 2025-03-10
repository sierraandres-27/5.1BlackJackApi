package cat.itacademy.s05.t01.n01.BlackJack.services;

import cat.itacademy.s05.t01.n01.BlackJack.Repository.PlayerRepository;
import cat.itacademy.s05.t01.n01.BlackJack.enums.PlayerType;
import cat.itacademy.s05.t01.n01.BlackJack.exceptions.PlayerNotFoundException;
import cat.itacademy.s05.t01.n01.BlackJack.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;



import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    private Player player;

    @BeforeEach
    void setUp() {

        //MockitoAnnotations.openMocks(this);

        //playerService = new PlayerService(playerRepository);

        player = Player.builder()
                .name("John")
                .player_type(PlayerType.PLAYER)
                .build();

    }

    @Test
    void createDealerTest() {

        Mono<Player> dealerMono = playerService.createDealer();

        StepVerifier.create(dealerMono)
                .expectNextMatches(dealer ->
                        dealer.getName().equals("dealer") &&
                                dealer.getPlayer_type() == PlayerType.DEALER)
                .verifyComplete();
    }

    @Test
    void createPlayerTest() {

        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(player));

        Mono<Player> result = playerService.createPlayer("John");

        StepVerifier.create(result)
                .expectNext(player)
                .verifyComplete();


    }

    @Test
    void updatePlayerTest (){

        Player exsitingPlayer = Player.builder()
                .id(1)
                .name("Jose")
                .player_type(PlayerType.PLAYER)
                .build();

        Player updatedPlayer = Player.builder()
                .id(1)
                .name("Marcos")
                .player_type(PlayerType.PLAYER)
                .build();

        when(playerRepository.findById(1)).thenReturn(Mono.just(exsitingPlayer));
        when(playerRepository.save(updatedPlayer)).thenReturn(Mono.just(updatedPlayer));

        Mono<Player>result = playerService.updateNamePlayer(1,"Marcos");

        StepVerifier.create(result)
                .expectNext(updatedPlayer)
                .verifyComplete();


    }


    @Test
    void updatePlayerErrorTest (){

        when(playerRepository.findById(1)).thenReturn(Mono.empty());

        Mono<Player>result=playerService.updateNamePlayer(1,"Marcos");

        StepVerifier.create(result)
                .expectError(PlayerNotFoundException.class)
                .verify();

    }

    @Test
    void testGetRanking_Success() {
        Player player1 = Player.builder().id(1).name("Alice").games_won(5).build();
        Player player2 = Player.builder().id(2).name("Bob").games_won(3).build();
        Player player3 = Player.builder().id(3).name("Charlie").games_won(7).build();

        List<Player> players = List.of(player1, player2, player3);

        when(playerRepository.findAll()).thenReturn(Flux.fromIterable(players));

        Flux<Player> result = playerService.getRanking();

        StepVerifier.create(result)
                .expectNextMatches(player -> player.getName().equals("Charlie") && player.getGames_won() == 7)
                .expectNextMatches(player -> player.getName().equals("Alice") && player.getGames_won() == 5)
                .expectNextMatches(player -> player.getName().equals("Bob") && player.getGames_won() == 3)
                .verifyComplete();

        verify(playerRepository, times(1)).findAll();
    }

    @Test
    void testGetRanking_Empty() {
        when(playerRepository.findAll()).thenReturn(Flux.empty());

        Flux<Player> result = playerService.getRanking();

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Ranking players not found"))
                .verify();

        verify(playerRepository, times(1)).findAll();
    }





}