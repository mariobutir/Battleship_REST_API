package mario.battleship.controllers;

import mario.battleship.domain.Conflict;
import mario.battleship.domain.Game;
import mario.battleship.domain.Player;
import mario.battleship.exceptions.UserOrGameDoesNotExistException;
import mario.battleship.mapper.GameMapper;
import mario.battleship.mapper.GameStatusMapper;
import mario.battleship.model.GameDTO;
import mario.battleship.model.GameStatusDTO;
import mario.battleship.model.NextTurnDTO;
import mario.battleship.model.WonDTO;
import mario.battleship.services.GameService;
import mario.battleship.services.PlayerService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class GameController {

    public static Conflict conflict;
    private final PlayerService playerService;
    private final GameService gameService;
    private final GameMapper gameMapper;
    private final GameStatusMapper gameStatusMapper;

    public GameController(PlayerService playerService, GameService gameService, GameMapper gameMapper,
                          GameStatusMapper gameStatusMapper) {
        this.playerService = playerService;
        this.gameService = gameService;
        this.gameMapper = gameMapper;
        this.gameStatusMapper = gameStatusMapper;
    }

    @PostMapping("/player/{opponent_id}/game")
    public GameDTO newGame(@RequestBody Player player, @PathVariable String opponent_id, HttpServletRequest request, HttpServletResponse response) {
        if ((playerService.getPlayerById(player.getId()) == null)) {
            conflict = new Conflict("error.unknown-user-id", player.getId().toString());
            throw new UserOrGameDoesNotExistException();

        } else if (playerService.getPlayerById(Long.valueOf(opponent_id)) == null) {
            conflict = new Conflict("error.unknown-user-id", opponent_id);
            throw new UserOrGameDoesNotExistException();

        } else {
            GameDTO savedGame = gameMapper.gameToGameDTO(gameService.createGame(player.getId().toString(), opponent_id));
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setHeader("Location", "/game/" + savedGame.getGame_id().toString());
            return savedGame;
        }
    }

    @GetMapping("/player/{player_id}/game/{game_id}")
    public GameStatusDTO gameStatus(@PathVariable Long player_id, @PathVariable Long game_id, HttpServletResponse response) {
        if ((playerService.getPlayerById(player_id) == null)) {
            conflict = new Conflict("error.unknown-user-id", player_id.toString());
            throw new UserOrGameDoesNotExistException();

        } else if (gameService.getGameById(game_id) == null) {
            conflict = new Conflict("error.unknown-game-id", game_id.toString());
            throw new UserOrGameDoesNotExistException();

        } else if (!(gameService.getGameById(game_id).getPlayerId().equals(player_id.toString()) || gameService.getGameById(game_id).getOpponentId().equals(player_id.toString()))){
            conflict = new Conflict("error.unknown-game-id", game_id.toString());
            throw new UserOrGameDoesNotExistException();

        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            Game game = gameService.getGameById(game_id);

            if (game.getWon() == null) {
                NextTurnDTO nextTurnDTO = new NextTurnDTO();
                nextTurnDTO.setPlayer_turn(game.getPlayerTurn());

                if (game.getPlayerId().equals(player_id.toString())) {
                    return gameStatusMapper.gameStatusToGameStatusDTO(
                            game.getPlayerGameStatus(), player_id, Long.valueOf(gameService.getGameById(game_id).getOpponentId()), nextTurnDTO);

                } else {
                    return gameStatusMapper.gameStatusToGameStatusDTO(
                            gameService.getGameById(game_id).getOpponentGameStatus(), Long.valueOf(gameService.getGameById(game_id).getOpponentId()), player_id, nextTurnDTO);
                }

            } else {
                WonDTO wonDTO = new WonDTO();
                wonDTO.setWon(game.getWon());

                if (game.getPlayerId().equals(player_id.toString())) {
                    return gameStatusMapper.gameStatusToGameStatusDTO(
                            game.getPlayerGameStatus(), player_id, Long.valueOf(gameService.getGameById(game_id).getOpponentId()), wonDTO);

                } else {
                    return gameStatusMapper.gameStatusToGameStatusDTO(
                            gameService.getGameById(game_id).getOpponentGameStatus(), Long.valueOf(gameService.getGameById(game_id).getOpponentId()), player_id, wonDTO);
                }
            }
        }
    }
}