package mario.battleship.repositories;

import mario.battleship.domain.GameStatus;
import mario.battleship.domain.PlayerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameStatusRepository extends JpaRepository<GameStatus, Long> {
    GameStatus findByGameIdAndSelf(String gameId, PlayerStatus self);
}
