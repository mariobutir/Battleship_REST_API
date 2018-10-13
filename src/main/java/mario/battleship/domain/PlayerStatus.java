package mario.battleship.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class PlayerStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerId;

    @OneToMany
    private List<BoardRow> board;

    private int remainingShips;
    private boolean autopilot;

}
