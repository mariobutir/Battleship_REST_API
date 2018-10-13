package mario.battleship.services;

import mario.battleship.domain.GameListObject;
import mario.battleship.repositories.GameListObjectRepository;
import org.springframework.stereotype.Service;

@Service
public class GameListObjectServiceImpl implements GameListObjectService {
    private final GameListObjectRepository gameListObjectRepository;

    public GameListObjectServiceImpl(GameListObjectRepository gameListObjectRepository) {
        this.gameListObjectRepository = gameListObjectRepository;
    }

    @Override
    public GameListObject createGameListObject(GameListObject gameListObject) {
        return gameListObjectRepository.saveAndFlush(gameListObject);
    }

    @Override
    public GameListObject findByGameIdAndOpponentId(Long gameId, Long opponentId) {
        return gameListObjectRepository.findByGameIdAndAndOpponentId(gameId, opponentId);
    }
}
