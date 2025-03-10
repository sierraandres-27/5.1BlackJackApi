package cat.itacademy.s05.t01.n01.BlackJack.controllers;


import cat.itacademy.s05.t01.n01.BlackJack.models.Player;
import cat.itacademy.s05.t01.n01.BlackJack.services.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
@Tag(name = "Player Controller", description = "End point related to de players of the game")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/ranking")
    @Operation(summary = "Get the ordered ranking of the players by his score",
               description = "Get a flux of a list of players ordered by his score, from the max to the lowest",
                responses = {
                @ApiResponse (responseCode = "200",description = "Correctly get it the list of the ranking"),
                        @ApiResponse(responseCode = "400",description = "Dont find any player score in the data base")
                }
    )
    public ResponseEntity<Flux<Player>> getRanking() {
        Flux<Player> ranking = playerService.getRanking();
        return ResponseEntity.ok(ranking);
    }

    @PutMapping("/player/{playerId}")
    @Operation(
            summary = "Actualizar nombre de un jugador",
            description = "Actualiza el nombre de un jugador existente identificado por su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "El jugador fue actualizado correctamente."),
                    @ApiResponse(responseCode = "404", description = "No se encontró el jugador con el ID proporcionado."),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud.")
            }
    )
    public Mono<ResponseEntity<Player>> updateNamePlayer(@PathVariable int playerId, @RequestBody Player player) {
        return playerService.updatePlayer(player)
                .map(updatedPlayer -> ResponseEntity.ok(updatedPlayer));
    }

}
