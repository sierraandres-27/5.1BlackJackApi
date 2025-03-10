package cat.itacademy.s05.t01.n01.BlackJack.services;

import cat.itacademy.s05.t01.n01.BlackJack.enums.Rank;
import cat.itacademy.s05.t01.n01.BlackJack.enums.Suit;
import cat.itacademy.s05.t01.n01.BlackJack.models.Card;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CardService {


    public Mono<List<Card>> getShuffledDeck() {
        List<Card> deck = new ArrayList<>();
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                deck.add(new Card(rank, suit));
            }
            Collections.shuffle(deck);
        }
        return Mono.just(deck);
    }

}
