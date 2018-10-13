package mario.battleship.mapper;

import mario.battleship.domain.PlayerList;
import mario.battleship.model.PlayerListDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PlayerListMapperImpl implements PlayerListMapper {
    private final PlayerListObjectMapper playerListObjectMapper;

    public PlayerListMapperImpl(PlayerListObjectMapper playerListObjectMapper) {
        this.playerListObjectMapper = playerListObjectMapper;
    }

    @Override
    public PlayerListDTO playerListToPlayerListDTO(PlayerList playerList) {
        if (playerList == null) {
            return null;
        }

        PlayerListDTO playerListDTO = new PlayerListDTO();
        playerListDTO.setPlayerDTOList(playerList.getPlayerList().stream().map(playerListObjectMapper::playerToPlayerListObjectDTO).collect(Collectors.toList()));

        return playerListDTO;
    }
}
