package mario.battleship.repositories;

import mario.battleship.domain.PlayerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerStatusRepository extends JpaRepository<PlayerStatus, Long> {
    PlayerStatus findByPlayerId(String id);
}
