package mario.battleship.repositories;

import mario.battleship.domain.PlayerList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerListRepository extends JpaRepository<PlayerList, Long> {
}
