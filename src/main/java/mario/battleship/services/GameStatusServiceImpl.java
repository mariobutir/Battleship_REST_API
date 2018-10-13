package mario.battleship.services;

import mario.battleship.domain.GameStatus;
import mario.battleship.domain.PlayerStatus;
import mario.battleship.repositories.GameStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class GameStatusServiceImpl implements GameStatusService {

    private final GameStatusRepository gameStatusRepository;

    public GameStatusServiceImpl(GameStatusRepository gameStatusRepository) {
        this.gameStatusRepository = gameStatusRepository;
    }

    @Override
    public GameStatus findById(Long id) {
        return gameStatusRepository.findById(id).orElse(null);
    }

    @Override
    public void createGameStatus(GameStatus gameStatus) {
        gameStatusRepository.saveAndFlush(gameStatus);
    }

    @Override
    public GameStatus findByGameIdAndSelf(String gameId, PlayerStatus self) {
        return gameStatusRepository.findByGameIdAndSelf(gameId, self);
    }
}
