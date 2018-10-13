package mario.battleship.services;

import mario.battleship.domain.GameStatus;
import mario.battleship.model.SalvoShotDTO;

public interface AutopilotService {

    SalvoShotDTO setSalvo(GameStatus attackerGameStatus, GameStatus opponentGameStatus);
}
