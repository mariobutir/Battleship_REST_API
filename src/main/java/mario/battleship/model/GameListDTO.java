package mario.battleship.model;

import lombok.Data;

import java.util.List;

@Data
public class GameListDTO {

    List<GameListObjectDTO> games;
}
