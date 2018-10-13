package mario.battleship.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerId;
    private String opponentId;
    private String playerTurn;
    private String won;

    @OneToMany
    private List<BoardRow> playerGrid;

    @OneToMany
    private List<BoardRow> opponentGrid;

    @ManyToOne(cascade = CascadeType.ALL)
    private GameStatus playerGameStatus;

    @ManyToOne(cascade = CascadeType.ALL)
    private GameStatus opponentGameStatus;
}
