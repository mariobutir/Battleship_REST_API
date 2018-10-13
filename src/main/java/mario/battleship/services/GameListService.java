package mario.battleship.services;

import mario.battleship.domain.GameList;

public interface GameListService {
    GameList createGameList(GameList gameList);

    GameList findById(Long id);
}
