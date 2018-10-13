package mario.battleship.model;

import lombok.Data;

import java.util.List;

@Data
public class PlayerStatusDTO {

    private String player_id;
    private List<String> board;
    private int remaining_ships;
    private boolean autopilot;
}
