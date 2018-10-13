package mario.battleship.services;

import mario.battleship.domain.BoardRow;
import mario.battleship.repositories.BoardRowRepository;
import org.springframework.stereotype.Service;

@Service
public class BoardRowServiceImpl implements BoardRowService {

    private final BoardRowRepository boardRowRepository;

    public BoardRowServiceImpl(BoardRowRepository boardRowRepository) {
        this.boardRowRepository = boardRowRepository;
    }

    @Override
    public BoardRow save(BoardRow boardRow) {
        return boardRowRepository.save(boardRow);
    }
}
