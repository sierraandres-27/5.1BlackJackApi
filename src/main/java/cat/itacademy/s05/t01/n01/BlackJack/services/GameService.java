package cat.itacademy.s05.t01.n01.BlackJack.services;


import cat.itacademy.s05.t01.n01.BlackJack.Repository.GameRepository;
import cat.itacademy.s05.t01.n01.BlackJack.enums.Rank;
import cat.itacademy.s05.t01.n01.BlackJack.exceptions.GameNotFoundException;
import cat.itacademy.s05.t01.n01.BlackJack.exceptions.PlayerNotFoundException;
import cat.itacademy.s05.t01.n01.BlackJack.models.Card;
import cat.itacademy.s05.t01.n01.BlackJack.models.Game;
import cat.itacademy.s05.t01.n01.BlackJack.models.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final CardService cardService;

    private final Map<Integer, List<Card>> playerHands = new ConcurrentHashMap<>();

    public Mono<Game> createGame (String playerName){
      return playerService.createPlayer(playerName)
              .flatMap(player->{
                      Game game= new Game();
                      game.setFinish(false);
                      game.setWinnerId(0);
                      game.getPlayersId().add(player.getId());
                  return gameRepository.save(game);
              });

  }

    public Mono<List<Card>> playGame(String gameId, int playerId) {
        return gameRepository.findById(gameId)
                .flatMap(game -> {
                    if (game.isFinish()) {
                        return Mono.error(new IllegalStateException("El juego ya ha finalizado."));
                    }
                    return makeHit(gameId, playerId);
                })
                .switchIfEmpty(Mono.error(new GameNotFoundException("Error findinf de game by id in playGame")));
    }

  public Mono<List<Card>> makeHit (String gameID, int playerId){

      return gameRepository.findById(gameID)
              .switchIfEmpty(Mono.error(new GameNotFoundException("not possible to make hit, id game not foundit ")))
              .flatMap(game -> {
                  if (game.getDeck()==null|| game.getDeck().isEmpty()){
                      return cardService.getShuffledDeck()
                              .flatMap(deck->{
                                  game.setDeck(deck);
                                  return extractCardAndUpdate(game,playerId);
                              });
                  }
                  return extractCardAndUpdate(game,playerId);

              });

  }
    private Mono<List<Card>> extractCardAndUpdate(Game game, int playerId) {
        Card card = game.getDeck().removeFirst();

        return playerService.findPlayerById(playerId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Player not found in extractCardAndUpdate")))
                .flatMap(player -> {
                    // Obtén la mano desde el mapa en memoria
                    List<Card> hand = playerHands.computeIfAbsent(playerId, k -> new ArrayList<>());
                    hand.add(card);
                    return gameRepository.save(game).then(Mono.just(hand));
                });
    }


    public Mono<String> makeStand(String gameId, int playerId) {
        return playerService.createDealer()
                .flatMap(dealer -> {
                    if (dealer.getHand() == null) {
                        dealer.setHand(new ArrayList<>()); // Inicializa la mano del dealer
                    }
                    return gameRepository.findById(gameId)
                            .switchIfEmpty(Mono.error(new GameNotFoundException("Error finding the id game to make stand")))
                            .flatMap(game -> makeDealerHand(game, dealer))
                            .flatMap(dealerHand -> getWinner(Mono.just(dealerHand), playerId, gameId));
                });
    }


    public Mono<List<Card>> makeDealerHand(Game game, Player dealer) {
        if (dealer.getHand() == null) {
            dealer.setHand(new ArrayList<>()); // Asegura que la mano esté inicializada
        }

        while (calculateHandValue(dealer.getHand()) < 17) {
            if (game.getDeck() == null || game.getDeck().isEmpty()) {
                return Mono.error(new IllegalStateException("The deck is empty in makeDealerHand"));
            }
            Card card = game.getDeck().removeFirst();
            dealer.getHand().add(card);
        }
        return Mono.just(dealer.getHand());
    }

  public int calculateHandValue (List<Card> hand){

      int totalValue = 0;
      int aceCount = 0;

      for (Card card: hand){
          Rank  rank = card.getRank();
          totalValue += rank.getValue();
          if (rank==Rank.ACE){
              aceCount+=1;
          }
      }
      while (totalValue > 21 & aceCount>0){
          totalValue-=10;
          aceCount--;
      }
      return totalValue;
  }





  public Mono<String> getWinner (Mono<List<Card>> monoDealerHand, int playerId, String gameId){
      return monoDealerHand.flatMap(dealerHand->
              gameRepository.findById(gameId)
                      .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found in getWinner")))
                      .flatMap(game -> playerService.findPlayerById(playerId)
                              .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player not found it in getWinner")))
                              .flatMap(player -> {
                                  List<Card> hand = playerHands.computeIfAbsent(playerId, k -> new ArrayList<>());
                                  int dealerScore = calculateHandValue(dealerHand);
                                  int playerScore= calculateHandValue(hand);

                                  String resultMessage;
                                  if (playerScore > 21) {
                                      resultMessage = "Dealer wins! Player bust. (Dealer: " + dealerScore + ", Player: " + playerScore + ")";
                                  } else if (dealerScore > 21) {
                                      resultMessage = "Player wins! Dealer bust. (Dealer: " + dealerScore + ", Player: " + playerScore + ")";
                                  } else if (playerScore > dealerScore) {
                                      resultMessage = "Player wins! (Dealer: " + dealerScore + ", Player: " + playerScore + ")";
                                  } else if (dealerScore > playerScore) {
                                      resultMessage = "Dealer wins! (Dealer: " + dealerScore + ", Player: " + playerScore + ")";
                                  } else {
                                      resultMessage = "It's a tie! (Dealer: " + dealerScore + ", Player: " + playerScore + ")";
                                  }

                                return updateInfo(resultMessage,player,game);

                              })

                      )

              );

  }

  public Mono<String> updateInfo (String resultMessage, Player player, Game game) {

      if (resultMessage.contains("Player wins")) {

          player.setGames_won(player.getGames_won() + 1);
          game.setWinnerId(player.getId());

      }
      game.setFinish(true);

      return playerService.updatePlayer(player)
              .flatMap(updatedPlayer -> gameRepository.save(game))
              .then(Mono.just(resultMessage));

  }

  public Mono<Void> deleteGame (String gameId){

      return gameRepository.deleteById(gameId)
              .onErrorMap(e->new GameNotFoundException("Game not found in delte game"));
  }

    public Mono<Game> getInfoGame(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found in getInfoGame")));


    }












}
