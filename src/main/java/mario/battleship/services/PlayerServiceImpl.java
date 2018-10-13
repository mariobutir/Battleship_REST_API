package mario.battleship.services;

import mario.battleship.domain.Player;
import mario.battleship.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Player createPlayer(Player player) {
        return playerRepository.saveAndFlush(player);
    }

    @Override
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @Override
    public Player getPlayerByEmail(String email) {

        return playerRepository.findPlayerByEmail(email);
    }
}
