package cat.itacademy.s05.t01.n01.BlackJack.models;

import cat.itacademy.s05.t01.n01.BlackJack.enums.PlayerType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table ("players")
@Builder
public class Player {

    @Id
    private Integer id;
    private String name;
    private PlayerType player_type;
    @Transient
    private List<Card> hand = new ArrayList<>();
    private int games_won;





}
