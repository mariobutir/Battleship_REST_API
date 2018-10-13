package mario.battleship.mapper;

import mario.battleship.domain.PlayerList;
import mario.battleship.model.PlayerListDTO;

public interface PlayerListMapper {
    PlayerListDTO playerListToPlayerListDTO(PlayerList playerList);
}
