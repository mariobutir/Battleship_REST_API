package mario.battleship.mapper;

import mario.battleship.domain.Player;
import mario.battleship.model.PlayerListObjectDTO;

public interface PlayerListObjectMapper {
    PlayerListObjectDTO playerToPlayerListObjectDTO(Player player);
}
