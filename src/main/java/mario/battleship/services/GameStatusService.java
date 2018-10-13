package mario.battleship.services;

import mario.battleship.domain.GameStatus;
import mario.battleship.domain.PlayerStatus;

public interface GameStatusService {

    void createGameStatus(GameStatus gameStatus);

    GameStatus findByGameIdAndSelf(String gameId, PlayerStatus self);

    GameStatus findById(Long id);
}
