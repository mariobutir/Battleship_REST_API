package mario.battleship.mapper;

import mario.battleship.domain.Player;
import mario.battleship.model.PlayerDTO;

public interface PlayerMapper {
    PlayerDTO playerToPlayerDTO(Player player);
}
