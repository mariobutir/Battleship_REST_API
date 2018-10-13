package mario.battleship.mapper;

import mario.battleship.domain.PlayerStatus;
import mario.battleship.model.PlayerStatusDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayerStatusMapperImpl implements PlayerStatusMapper {
    @Override
    public PlayerStatusDTO playerStatusToPlayerStatusDTO(PlayerStatus playerStatus) {
        if (playerStatus == null) {
            return null;
        }

        PlayerStatusDTO playerStatusDTO = new PlayerStatusDTO();
        List<String> board = new ArrayList<>();

        playerStatus.getBoard().iterator().forEachRemaining(boardRow -> board.add(boardRow.getRow()));
        playerStatusDTO.setBoard(board);
        playerStatusDTO.setPlayer_id(playerStatus.getPlayerId());
        playerStatusDTO.setRemaining_ships(playerStatus.getRemainingShips());
        playerStatusDTO.setAutopilot(playerStatus.isAutopilot());

        return playerStatusDTO;
    }
}
