package mario.battleship.mapper;

import mario.battleship.domain.Player;
import mario.battleship.model.PlayerDTO;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapperImpl implements PlayerMapper {

    @Override
    public PlayerDTO playerToPlayerDTO(Player player) {
        if (player == null) {
            return null;
        }

        PlayerDTO playerDTO = new PlayerDTO();

        playerDTO.name = player.name;
        playerDTO.email = player.email;

        return playerDTO;
    }
}
