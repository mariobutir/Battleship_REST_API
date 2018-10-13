package mario.battleship.services;

import mario.battleship.domain.Player;

public interface PlayerService {

    Player createPlayer(Player player);
    Player getPlayerById(Long id);
    Player getPlayerByEmail(String email);

}
