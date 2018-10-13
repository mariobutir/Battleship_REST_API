package mario.battleship.mapper;

import mario.battleship.domain.PlayerStatus;
import mario.battleship.model.PlayerStatusDTO;

public interface PlayerStatusMapper {
    PlayerStatusDTO playerStatusToPlayerStatusDTO(PlayerStatus playerStatus);
}
