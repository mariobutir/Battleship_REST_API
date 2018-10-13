package mario.battleship.repositories;

import mario.battleship.domain.BoardRow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRowRepository extends JpaRepository<BoardRow, Long> {
}
