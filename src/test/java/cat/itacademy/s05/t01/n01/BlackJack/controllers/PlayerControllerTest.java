package cat.itacademy.s05.t01.n01.BlackJack.controllers;

import cat.itacademy.s05.t01.n01.BlackJack.enums.PlayerType;
import cat.itacademy.s05.t01.n01.BlackJack.models.Player;
import cat.itacademy.s05.t01.n01.BlackJack.services.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
@WebFluxTest (controllers = PlayerController.class)
class PlayerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getRanking() {

        Player player1 = new Player(1,"Marga", PlayerType.PLAYER, null, 2);
        Player player2 = new Player(4,"Maria", PlayerType.PLAYER, null, 4);

        Flux<Player> playerFlux = Flux.fromIterable(Arrays.asList(player1,player2));

        when(playerService.getRanking()).thenReturn(playerFlux);

        webTestClient.get()
                .uri("/players/ranking")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Player.class)
                .hasSize(2)
                .contains(player1, player2);

    }

    @Test
    void updateNamePlayer() {

        int playerId = 1;
        Player playerInput = new Player(playerId, "NewName", PlayerType.PLAYER, null, 0);
        Player playerUpdated = new Player(playerId, "NewName", PlayerType.PLAYER, null, 0);

        when(playerService.updatePlayer(playerInput)).thenReturn(Mono.just(playerUpdated));

        webTestClient.put()
                .uri("/players/player/{playerId}", playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(playerInput)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Player.class)
                .isEqualTo(playerUpdated);
    }
}