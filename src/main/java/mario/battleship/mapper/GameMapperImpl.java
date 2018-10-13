package mario.battleship.mapper;

import mario.battleship.domain.Game;
import mario.battleship.model.GameDTO;
import org.springframework.stereotype.Component;

@Component
public class GameMapperImpl implements GameMapper {

    @Override
    public GameDTO gameToGameDTO(Game game) {
        if (game == null) {
            return null;
        }

        GameDTO gameDTO = new GameDTO();

        gameDTO.setGame_id(game.getId());
        gameDTO.setPlayer_id(game.getPlayerId());
        gameDTO.setOpponent_id(game.getOpponentId());
        gameDTO.setStarting(game.getPlayerTurn());

        return gameDTO;
    }
}
