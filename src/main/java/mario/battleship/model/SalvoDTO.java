package mario.battleship.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class SalvoDTO {

    private Map<String, String> salvo = new HashMap<>();
    private NextTurnOrWon game;
}
