package mario.battleship.model;

import lombok.Data;

@Data
public class GameListObjectDTO {

    private String game_id;
    private String opponent_id;
    private String status;
}
