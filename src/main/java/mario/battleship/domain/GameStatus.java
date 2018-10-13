package mario.battleship.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GameStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gameId;

    @ManyToOne(cascade = CascadeType.MERGE)
    private PlayerStatus self;

    @ManyToOne(cascade = CascadeType.MERGE)
    private PlayerStatus opponent;

    private boolean autopilot;

    private int remainingShips;
}
