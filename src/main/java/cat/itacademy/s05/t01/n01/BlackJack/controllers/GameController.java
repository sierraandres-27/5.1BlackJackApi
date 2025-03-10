package cat.itacademy.s05.t01.n01.BlackJack.controllers;


import cat.itacademy.s05.t01.n01.BlackJack.models.Card;
import cat.itacademy.s05.t01.n01.BlackJack.models.Game;
import cat.itacademy.s05.t01.n01.BlackJack.services.GameService;
import cat.itacademy.s05.t01.n01.BlackJack.services.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;

@RestController
@RequestMapping ("/game")
@RequiredArgsConstructor
@Tag(name = "Game Controller", description = "Gestiona las operaciones relacionadas con las partidas del juego.")
public class GameController {

    private final GameService gameService;

    @PostMapping("/new")
    @Operation(summary = "Create a new game",
            description = "create a new game and associate it with the given player name",
            responses = {
            @ApiResponse(responseCode = "201",description = "Partida creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Invalid petition")
            }
    )
    public Mono<ResponseEntity<Game>> createGame(@RequestBody String playerName) {
        return gameService.createGame(playerName)
                .map(game -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(game));
    }


    @GetMapping("/{gameId}")
    @Operation(
            summary = "Obtener información de una partida",
            description = "Devuelve los detalles de una partida específica basada en su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Información de la partida obtenida correctamente."),
                    @ApiResponse(responseCode = "404", description = "No se encontró la partida con el ID proporcionado.")
            }
    )
    public Mono<ResponseEntity<Game>> getInfoGame (@PathVariable String gameId){

        return gameService.getInfoGame(gameId)
                .map(game -> ResponseEntity
                                .status(HttpStatus.OK)
                                .body(game));

    }

    @PostMapping("/{gameId}/play/{playerId}")
    @Operation(
            summary = "Iniciar una partida",
            description = "Permite que un jugador comience una partida de Blackjack y recibe las cartas iniciales.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "El jugador ha comenzado la partida correctamente."),
                    @ApiResponse(responseCode = "404", description = "No se encontró la partida o el jugador con los IDs proporcionados.")
            }
    )
    public Mono<ResponseEntity<List<Card>>> playGame(@PathVariable String gameId, @PathVariable int playerId) {
        return gameService.playGame(gameId, playerId)
                .map(cards -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(cards));
    }

    @PostMapping("/{gameId}/player/{playerId}/hit")
    @Operation(
            summary = "Solicitar una carta ('Hit')",
            description = "El jugador solicita una carta adicional durante la partida.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Carta añadida correctamente a la mano del jugador."),
                    @ApiResponse(responseCode = "404", description = "No se encontró la partida o el jugador con los IDs proporcionados.")
            }
    )
    public Mono<ResponseEntity<List<Card>>> makeHit(@PathVariable String gameId, @PathVariable int playerId) {
        return gameService.makeHit(gameId, playerId)
                .map(cards -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(cards));
    }


    @PostMapping("/{gameId}/stand/{playerId}")
    @Operation(
            summary = "Finalizar turno ('Stand')",
            description = "El jugador decide finalizar su turno y no solicitar más cartas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Turno finalizado correctamente."),
                    @ApiResponse(responseCode = "404", description = "No se encontró la partida o el jugador con los IDs proporcionados.")
            }
    )
    public Mono<ResponseEntity<String>> makeStand(@PathVariable String gameId, @PathVariable int playerId) {
        return gameService.makeStand(gameId, playerId)
                .map(resultMessage -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(resultMessage));

    }


    @DeleteMapping("/{gameId}/delete")
    @Operation(
            summary = "Eliminar una partida",
            description = "Elimina una partida específica identificada por su ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Partida eliminada correctamente."),
                    @ApiResponse(responseCode = "404", description = "No se encontró la partida con el ID proporcionado.")
            }
    )
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable String gameId) {
        return gameService.deleteGame(gameId)
                .then(Mono.just(ResponseEntity.noContent().build()));

    }



}


