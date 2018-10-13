package mario.battleship.repositories;

import mario.battleship.domain.GameListObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameListObjectRepository extends JpaRepository<GameListObject, Long> {

    GameListObject findByGameIdAndAndOpponentId(Long gameId, Long opponentId);
}
