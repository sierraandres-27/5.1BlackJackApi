package cat.itacademy.s05.t01.n01.BlackJack.models;


import io.r2dbc.spi.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "games")
public class Game {

    @Id
    private String id;
    private boolean finish=false;
    private int winnerId;
    private List<Integer>playersId=new ArrayList<>();
    private List<Card>deck=new ArrayList<>();





}
