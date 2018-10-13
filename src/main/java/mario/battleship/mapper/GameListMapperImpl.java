package mario.battleship.mapper;

import mario.battleship.domain.GameList;
import mario.battleship.model.GameListDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GameListMapperImpl implements GameListMapper {
    private final GameListObjectMapper gameListObjectMapper;

    public GameListMapperImpl(GameListObjectMapper gameListObjectMapper) {
        this.gameListObjectMapper = gameListObjectMapper;
    }

    @Override
    public GameListDTO gameListToGameListDTO(GameList gameList) {
        if (gameList == null) {
            return null;
        }

        GameListDTO gameListDTO = new GameListDTO();
        gameListDTO.setGames(gameList.getList().stream().map(gameListObjectMapper::gameListObjectToGameListObjectDTO).collect(Collectors.toList()));

        return gameListDTO;
    }
}
