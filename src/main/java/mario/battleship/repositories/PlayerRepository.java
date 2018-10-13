package mario.battleship.repositories;

import mario.battleship.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findPlayerByEmail(String email);
}
