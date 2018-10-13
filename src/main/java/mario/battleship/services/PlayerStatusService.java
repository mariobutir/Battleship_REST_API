package mario.battleship.services;

import mario.battleship.domain.PlayerStatus;

import java.util.List;

public interface PlayerStatusService {

    PlayerStatus createPlayerStatus(PlayerStatus playerStatus);

    List<PlayerStatus> getAll();
}
