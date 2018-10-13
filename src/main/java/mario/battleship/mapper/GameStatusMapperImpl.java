package mario.battleship.mapper;

import mario.battleship.domain.GameStatus;
import mario.battleship.model.GameStatusDTO;
import mario.battleship.model.NextTurnOrWon;
import mario.battleship.model.PlayerStatusDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GameStatusMapperImpl implements GameStatusMapper {

    private final PlayerStatusMapper playerStatusMapper;

    public GameStatusMapperImpl(PlayerStatusMapper playerStatusMapper) {
        this.playerStatusMapper = playerStatusMapper;
    }

    @Override
    public GameStatusDTO gameStatusToGameStatusDTO(GameStatus gameStatus, Long playerId, Long opponentId, NextTurnOrWon nextTurnOrWon) {
        if ((gameStatus == null) || (playerId == null) || (opponentId == null)) {
            return null;
        }

        GameStatusDTO gameStatusDTO = new GameStatusDTO();

        PlayerStatusDTO self = playerStatusMapper.playerStatusToPlayerStatusDTO(gameStatus.getSelf());
        PlayerStatusDTO opponent = playerStatusMapper.playerStatusToPlayerStatusDTO(gameStatus.getOpponent());

        gameStatusDTO.setSelf(self);
        gameStatusDTO.setOpponent(opponent);
        gameStatusDTO.setGame(nextTurnOrWon);

        opponent.setBoard(opponent.getBoard().stream().map(s -> {
            StringBuilder row = new StringBuilder(s);

            for (int i = 0; i < s.length(); i++) {
                if (String.valueOf(row.charAt(i)).equals("#") || String.valueOf(row.charAt(i)).equals(".")) {
                    row.setCharAt(i, '.');
                }
            }
            return row.toString();
        }).collect(Collectors.toList()));

        gameStatusDTO.setGame(nextTurnOrWon);

        return gameStatusDTO;

    }
}