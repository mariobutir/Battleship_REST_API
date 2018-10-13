package mario.battleship.mapper;

import mario.battleship.domain.GameList;
import mario.battleship.model.GameListDTO;

public interface GameListMapper {
    GameListDTO gameListToGameListDTO(GameList gameList);

}
