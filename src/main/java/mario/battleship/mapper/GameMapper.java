package mario.battleship.mapper;

import mario.battleship.domain.Game;
import mario.battleship.model.GameDTO;

public interface GameMapper {
    GameDTO gameToGameDTO(Game game);
}
