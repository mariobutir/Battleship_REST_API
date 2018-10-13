package mario.battleship.model;

import lombok.Data;

@Data
public class GameDTO {

    private Long game_id;
    private String player_id;
    private String opponent_id;
    private String starting;
}
