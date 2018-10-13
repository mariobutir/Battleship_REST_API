package mario.battleship.services;

import mario.battleship.domain.PlayerList;
import mario.battleship.repositories.PlayerListRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerListServiceImpl implements PlayerListService {
    private final PlayerListRepository playerListRepository;

    public PlayerListServiceImpl(PlayerListRepository playerListRepository) {
        this.playerListRepository = playerListRepository;
    }

    @Override
    public PlayerList save(PlayerList playerList) {
        return playerListRepository.save(playerList);
    }

    @Override
    public PlayerList getList() {
        try {
            return playerListRepository.findAll().get(0);
        } catch (Exception e) {
            return new PlayerList();
        }
    }
}
