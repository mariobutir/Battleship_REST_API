package mario.battleship.mapper;

import mario.battleship.domain.Player;
import mario.battleship.model.PlayerListObjectDTO;
import org.springframework.stereotype.Component;

@Component
public class PlayerListObjectMapperImpl implements PlayerListObjectMapper {

    @Override
    public PlayerListObjectDTO playerToPlayerListObjectDTO(Player player) {
        if (player == null) {
            return null;
        }

        PlayerListObjectDTO playerListObjectDTO = new PlayerListObjectDTO();

        playerListObjectDTO.setId(player.getId().toString());
        playerListObjectDTO.setName(player.getName());
        playerListObjectDTO.setEmail(player.getEmail());

        return playerListObjectDTO;
    }
}
