package mario.battleship.services;

import mario.battleship.domain.GameListObject;

public interface GameListObjectService {

    GameListObject createGameListObject(GameListObject gameListObject);

    GameListObject findByGameIdAndOpponentId(Long gameId, Long opponentId);
}
