package mario.battleship.services;

import mario.battleship.domain.Game;

public interface GameService {

    Game createGame(String player_id, String opponent_id);

    Game saveGame(Game game);

    Game getGameById(Long id);
}
