package cat.itacademy.s05.t01.n01.BlackJack.models;

import cat.itacademy.s05.t01.n01.BlackJack.enums.Rank;
import cat.itacademy.s05.t01.n01.BlackJack.enums.Suit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {


    private Rank rank;
    private Suit suit;

    public int cardValue (){

        return rank.getValue();
    }




}
