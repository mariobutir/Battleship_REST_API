package mario.battleship.mapper;

import mario.battleship.domain.GameStatus;
import mario.battleship.model.GameStatusDTO;
import mario.battleship.model.NextTurnOrWon;

public interface GameStatusMapper {

    GameStatusDTO gameStatusToGameStatusDTO(GameStatus gameStatus, Long playerId, Long opponentId, NextTurnOrWon nextTurnOrWon);
}
