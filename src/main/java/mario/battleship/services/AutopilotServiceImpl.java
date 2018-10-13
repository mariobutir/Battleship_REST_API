package mario.battleship.services;

import mario.battleship.domain.GameStatus;
import mario.battleship.model.SalvoShotDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AutopilotServiceImpl implements AutopilotService {

    @Override
    public SalvoShotDTO setSalvo(GameStatus attackerGameStatus, GameStatus opponentGameStatus) {

        SalvoShotDTO salvoShot = new SalvoShotDTO();
        int numberOfAttackerShips = attackerGameStatus.getRemainingShips();

        String[] shots = new String[numberOfAttackerShips];

        for (int i = 0; i < numberOfAttackerShips; i++) {
            int row = (int) (Math.random() * 10);
            int column = (int) (Math.random() * 10);

            List<String> opponentBoard = new ArrayList<>();
            opponentGameStatus.getSelf().getBoard().iterator().forEachRemaining(boardRow -> opponentBoard.add(boardRow.getRow()));

            char c = opponentBoard.get(row).charAt(column);

            if (c != 'X' && c != 'O') {
                shots[i] = String.valueOf(row) + "x" + (char) (column + 65);

            } else {
                i--;
            }
        }

        salvoShot.setSalvo(shots);

        return salvoShot;
    }
}
