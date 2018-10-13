package mario.battleship.mapper;

import mario.battleship.domain.GameListObject;
import mario.battleship.model.GameListObjectDTO;

public interface GameListObjectMapper {

    GameListObjectDTO gameListObjectToGameListObjectDTO(GameListObject gameListObject);
}
