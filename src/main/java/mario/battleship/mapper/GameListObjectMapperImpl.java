package mario.battleship.mapper;

import mario.battleship.domain.GameListObject;
import mario.battleship.model.GameListObjectDTO;
import org.springframework.stereotype.Component;

@Component
public class GameListObjectMapperImpl implements GameListObjectMapper {

    @Override
    public GameListObjectDTO gameListObjectToGameListObjectDTO(GameListObject gameListObject) {
        if (gameListObject == null) {
            return null;
        }

        GameListObjectDTO gameListObjectDTO = new GameListObjectDTO();

        gameListObjectDTO.setGame_id(gameListObject.getGameId().toString());
        gameListObjectDTO.setOpponent_id(gameListObject.getOpponentId().toString());
        gameListObjectDTO.setStatus(gameListObject.getStatus());

        return gameListObjectDTO;
    }
}
