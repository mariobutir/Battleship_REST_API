package mario.battleship.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NextTurnDTO extends NextTurnOrWon {

    private String player_turn;
}
