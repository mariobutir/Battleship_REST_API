package mario.battleship.model;

import lombok.Data;

@Data
public class GameStatusDTO {

    private PlayerStatusDTO self;
    private PlayerStatusDTO opponent;
    private NextTurnOrWon game;
}
