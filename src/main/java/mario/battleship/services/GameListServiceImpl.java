package mario.battleship.services;

import mario.battleship.domain.GameList;
import mario.battleship.repositories.GameListRepository;
import org.springframework.stereotype.Service;

@Service
public class GameListServiceImpl implements GameListService {
    private final GameListRepository gameListRepository;

    public GameListServiceImpl(GameListRepository gameListRepository) {
        this.gameListRepository = gameListRepository;
    }

    @Override
    public GameList createGameList(GameList gameList) {
        return gameListRepository.saveAndFlush(gameList);
    }

    @Override
    public GameList findById(Long id) {
        return gameListRepository.findById(id).orElse(null);
    }
}
