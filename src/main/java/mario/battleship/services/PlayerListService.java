package mario.battleship.services;

import mario.battleship.domain.PlayerList;

public interface PlayerListService {
    PlayerList save(PlayerList playerList);

    PlayerList getList();
}
