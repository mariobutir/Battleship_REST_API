package mario.battleship.services;

import mario.battleship.domain.PlayerStatus;
import mario.battleship.repositories.PlayerStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerStatusServiceImpl implements PlayerStatusService {

    private final PlayerStatusRepository playerStatusRepository;

    public PlayerStatusServiceImpl(PlayerStatusRepository playerStatusRepository) {
        this.playerStatusRepository = playerStatusRepository;
    }

    @Override
    public PlayerStatus createPlayerStatus(PlayerStatus playerStatus) {
        return playerStatusRepository.saveAndFlush(playerStatus);
    }

    @Override
    public List<PlayerStatus> getAll() {
        return playerStatusRepository.findAll();
    }
}
