package mario.battleship.controllers;

import com.google.gson.Gson;
import mario.battleship.configuration.AutopilotHandler;
import mario.battleship.domain.*;
import mario.battleship.exceptions.*;
import mario.battleship.mapper.GameListMapper;
import mario.battleship.mapper.PlayerListMapper;
import mario.battleship.mapper.PlayerMapper;
import mario.battleship.model.*;
import mario.battleship.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PlayerController {

    public static Conflict conflict;
    private final PlayerService playerService;
    private final PlayerMapper playerMapper;
    private final GameListMapper gameListMapper;
    private final GameService gameService;
    private final PlayerStatusService playerStatusService;
    private final GameStatusService gameStatusService;
    private final AutopilotService autopilotService;
    private final GameListObjectService gameListObjectService;
    private final BoardRowService boardRowService;
    private final PlayerListService playerListService;
    private final PlayerListMapper playerListMapper;
    private final GameListService gameListService;
    private final GameController gameController;
    private final AutopilotHandler autopilotHandler;

    public PlayerController(PlayerService playerService, PlayerMapper playerMapper, GameListMapper gameListMapper, GameService gameService, PlayerStatusService playerStatusService,
                            GameStatusService gameStatusService, AutopilotService autopilotService, GameListObjectService gameListObjectService, BoardRowService boardRowService,
                            PlayerListService playerListService, PlayerListMapper playerListMapper, GameListService gameListService, GameController gameController,
                            AutopilotHandler autopilotHandler) {
        this.playerService = playerService;
        this.playerMapper = playerMapper;
        this.gameListMapper = gameListMapper;
        this.gameService = gameService;
        this.playerStatusService = playerStatusService;
        this.gameStatusService = gameStatusService;
        this.autopilotService = autopilotService;
        this.gameListObjectService = gameListObjectService;
        this.boardRowService = boardRowService;
        this.playerListService = playerListService;
        this.playerListMapper = playerListMapper;
        this.gameListService = gameListService;
        this.gameController = gameController;
        this.autopilotHandler = autopilotHandler;
    }

    @GetMapping("/player/{player_id}")
    @ResponseStatus(HttpStatus.OK)
    public PlayerDTO getPlayerById(@PathVariable Long player_id) {

        if (playerService.getPlayerById(player_id) == null) {
            throw new DoesNotExistException();

        } else {
            return playerMapper.playerToPlayerDTO(playerService.getPlayerById(player_id));
        }
    }

    @PostMapping("/player")
    public void newPlayer(@RequestBody Player player, HttpServletResponse response) {

        if (playerService.getPlayerByEmail(player.getEmail()) != null) {
            conflict = new Conflict("error.username-already-taken", player.getEmail());
            throw new EmailAlreadyExistsException();

        } else {
            Player savedPlayer = playerService.createPlayer(player);

            PlayerList playerList = new PlayerList();
            List<Player> list = playerList.getPlayerList();

            try {
                playerList = playerListService.getList();
                list = playerList.getPlayerList();
            } catch (Exception e) {
            }

            list.add(savedPlayer);
            playerList.setPlayerList(list);
            playerListService.save(playerList);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setHeader("Location", "/player/" + savedPlayer.getId());
        }
    }

    @GetMapping("/player/list")
    public PlayerListDTO getPlayers(HttpServletResponse response) {
        if (playerListService.getList() == null) {
            throw new NoContentException();

        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return playerListMapper.playerListToPlayerListDTO(playerListService.getList());
        }
    }

    @GetMapping("/player/{player_id}/game/list")
    public GameListDTO getPlayerGames(@PathVariable Long player_id, HttpServletResponse response) {
        if (playerService.getPlayerById(player_id) == null) {
            throw new DoesNotExistException();

        } else if (playerService.getPlayerById(player_id).getGameList() == null) {
            throw new NoContentException();

        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            return gameListMapper.gameListToGameListDTO(playerService.getPlayerById(player_id).getGameList());
        }
    }

    @PutMapping("/player/{player_id}/game/{game_id}")
    public SalvoDTO shotSalvo(@RequestBody SalvoShotDTO salvo, @PathVariable Long player_id, @PathVariable Long game_id, HttpServletResponse response) {

        SalvoDTO salvoDTO = new SalvoDTO();
        Game game = gameService.getGameById(game_id);
        int salvoLength = salvo.getSalvo().length;

        if (game == null || playerService.getPlayerById(player_id) == null) {
            throw new DoesNotExistException();

        } else if (!game.getPlayerTurn().equals(player_id.toString())) {
            throw new NotYourTurnException();

        } else if (game.getWon() != null) {
            throw new GameIsFinishedException();

        } else {
            if (player_id.toString().equals(game.getPlayerId())) {
                String opponentId = game.getOpponentId();

                game.setPlayerTurn(opponentId);
                salvoDTO.setGame(new NextTurnDTO(opponentId));

            } else {
                String playerId = game.getPlayerId();

                game.setPlayerTurn(playerId);
                salvoDTO.setGame(new NextTurnDTO(playerId));
            }
        }

        //Who is attacking
        String attacker;
        if (Long.valueOf(game.getPlayerId()).equals(player_id)) {
            attacker = "self";

            if (salvoLength > game.getPlayerGameStatus().getRemainingShips()) {
                throw new InvalidSalvoException();
            }

        } else {
            attacker = "opponent";

            if (salvoLength > game.getOpponentGameStatus().getRemainingShips()) {
                throw new InvalidSalvoException();
            }
        }

        int rows[] = new int[salvoLength];
        int columns[] = new int[salvoLength];

        //Get row and column that is shot at
        for (int i = 0; i < salvoLength; i++) {
            try {
                String[] temp = salvo.getSalvo()[i].trim().split("x");
                if (temp[1].length() > 1) {
                    throw new InvalidSalvoException();
                }

                rows[i] = Integer.parseInt(temp[0]);
                columns[i] = (int) Character.toUpperCase(temp[1].charAt(0)) - 'A';

            } catch (NumberFormatException e) {
                throw new InvalidSalvoException();
            }
        }

        //Get opponent board
        if (attacker.equals("self")) {
            GameStatus selfGameStatus = game.getPlayerGameStatus();
            GameStatus opponentGameStatus = game.getOpponentGameStatus();
            PlayerStatus opponent = opponentGameStatus.getSelf();
            List<String> board = new ArrayList<>();
            opponent.getBoard().iterator().forEachRemaining(boardRow -> board.add(boardRow.getRow()));

            checkRemainingShipsAndAutopilot(salvo, game_id, response, salvoDTO, game, salvoLength, rows, columns, selfGameStatus, opponentGameStatus, opponent, board);

        } else {
            GameStatus selfGameStatus = game.getOpponentGameStatus();
            GameStatus opponentGameStatus = game.getPlayerGameStatus();
            PlayerStatus opponent = opponentGameStatus.getSelf();
            List<String> board = new ArrayList<>();
            opponent.getBoard().iterator().forEachRemaining(boardRow -> board.add(boardRow.getRow()));

            checkRemainingShipsAndAutopilot(salvo, game_id, response, salvoDTO, game, salvoLength, rows, columns, selfGameStatus, opponentGameStatus, opponent, board);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return salvoDTO;
    }

    @PutMapping("/player/{player_id}/game/{game_id}/autopilot")
    public void setAutopilot(@PathVariable Long player_id, @PathVariable Long game_id, HttpServletResponse response) {
        Game game = gameService.getGameById(game_id);

        if ((playerService.getPlayerById(player_id) == null)) {
            conflict = new Conflict("error.unknown-user-id", player_id.toString());
            throw new UserOrGameDoesNotExistException();

        } else if (game == null) {
            conflict = new Conflict("error.unknown-game-id", game_id.toString());
            throw new UserOrGameDoesNotExistException();

        } else if (game.getWon() != null) {
            throw new GameIsFinishedException();

        } else {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

            if (Long.valueOf(game.getPlayerId()).equals(player_id)) {
                GameStatus playerGameStatus = game.getPlayerGameStatus();
                playerGameStatus.setAutopilot(true);
                playerGameStatus.getSelf().setAutopilot(true);
                gameStatusService.createGameStatus(playerGameStatus);

            } else {
                GameStatus opponentGameStatus = game.getOpponentGameStatus();
                opponentGameStatus.setAutopilot(true);
                opponentGameStatus.getSelf().setAutopilot(true);
                gameStatusService.createGameStatus(opponentGameStatus);
            }
        }
    }

    @PutMapping("/player/{player_id}/game/{game_id}/autopilot/off")
    public void setAutopilotOff(@PathVariable Long player_id, @PathVariable Long game_id, HttpServletResponse response) {
        Game game = gameService.getGameById(game_id);

        if ((playerService.getPlayerById(player_id) == null)) {
            conflict = new Conflict("error.unknown-user-id", player_id.toString());
            throw new UserOrGameDoesNotExistException();

        } else if (game == null) {
            conflict = new Conflict("error.unknown-game-id", game_id.toString());
            throw new UserOrGameDoesNotExistException();

        } else if (game.getWon() != null) {
            throw new GameIsFinishedException();

        } else {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

            if (Long.valueOf(game.getPlayerId()).equals(player_id)) {
                GameStatus playerGameStatus = game.getPlayerGameStatus();
                playerGameStatus.setAutopilot(false);
                playerGameStatus.getSelf().setAutopilot(false);
                gameStatusService.createGameStatus(playerGameStatus);

            } else {
                GameStatus opponentGameStatus = game.getOpponentGameStatus();
                opponentGameStatus.setAutopilot(false);
                opponentGameStatus.getSelf().setAutopilot(false);
                gameStatusService.createGameStatus(opponentGameStatus);
            }
        }
    }

    private void checkRemainingShipsAndAutopilot(SalvoShotDTO salvo, Long game_id, HttpServletResponse response, SalvoDTO salvoDTO, Game game, int salvoLength, int[] rows, int[] columns,
                                                 GameStatus selfGameStatus, GameStatus opponentGameStatus, PlayerStatus opponent, List<String> board) {
        if (opponentGameStatus.getRemainingShips() <= 0) {
            game.setWon(selfGameStatus.getSelf().getPlayerId());
            salvoDTO.setGame(new WonDTO(selfGameStatus.getSelf().getPlayerId()));

            GameListObject playerGameListObject = gameListObjectService.findByGameIdAndOpponentId(game_id, Long.valueOf(opponent.getPlayerId()));
            playerGameListObject.setStatus("WON");

            GameListObject opponentGameListObject = gameListObjectService.findByGameIdAndOpponentId(game_id, Long.valueOf(selfGameStatus.getSelf().getPlayerId()));
            opponentGameListObject.setStatus("LOST");

            gameListObjectService.createGameListObject(playerGameListObject);
            gameListObjectService.createGameListObject(opponentGameListObject);
            gameService.saveGame(game);

        } else if (selfGameStatus.getRemainingShips() <= 0) {
            game.setWon(opponentGameStatus.getSelf().getPlayerId());
            salvoDTO.setGame(new WonDTO(opponentGameStatus.getSelf().getPlayerId()));

            GameListObject opponentGameListObject = gameListObjectService.findByGameIdAndOpponentId(game_id, Long.valueOf(selfGameStatus.getSelf().getPlayerId()));
            opponentGameListObject.setStatus("WON");

            GameListObject playerGameListObject = gameListObjectService.findByGameIdAndOpponentId(game_id, Long.valueOf(opponent.getPlayerId()));
            playerGameListObject.setStatus("LOST");

            gameListObjectService.createGameListObject(playerGameListObject);
            gameListObjectService.createGameListObject(opponentGameListObject);
            gameService.saveGame(game);

        } else {

            checkShot(salvo, salvoDTO, salvoLength, rows, columns, board, opponentGameStatus, opponent, game_id);

            if(selfGameStatus.getSelf().isAutopilot() && opponentGameStatus.getSelf().isAutopilot()) {
                Gson gson = new Gson();
                TextMessage textMessage = new TextMessage(gson.toJson(gameController.gameStatus(Long.valueOf(selfGameStatus.getSelf().getPlayerId()), game_id, response)));
                try {
                    autopilotHandler.handleTextMessage(textMessage);
                } catch (Exception e) {
                    System.out.println("websocket is closed");
                }

                try {
                    Thread.sleep(2L * 1000L);
                } catch (InterruptedException e) {
                    System.out.println("autoilot handler - thread sleep interrupted");
                }
            }

            if (opponentGameStatus.getSelf().isAutopilot()){
                shotSalvo(autopilotService.setSalvo(opponentGameStatus, selfGameStatus), Long.valueOf(opponent.getPlayerId()), game_id, response);
            }
        }
    }

    private void checkShot(SalvoShotDTO salvo, SalvoDTO salvoDTO, int arrayLength, int[] rows, int[] columns, List<String> board,
                           GameStatus gameStatus, PlayerStatus attackedPlayerStatus, Long gameId) {

        for (int i = 0; i < arrayLength; i++) {
            boolean killed = true;
            int j = 1;

            String cell = String.valueOf(board.get(rows[i]).charAt(columns[i]));

            if (cell.equals("#")) {
                StringBuilder boardRow = new StringBuilder(board.get(rows[i]));

                boardRow.setCharAt(columns[i], 'X');
                board.set(rows[i], boardRow.toString());

                List<BoardRow> boardRowList = getBoardRows(board);

                attackedPlayerStatus.setBoard(boardRowList);
                playerStatusService.createPlayerStatus(attackedPlayerStatus);

                gameStatus.setSelf(attackedPlayerStatus);
                gameStatusService.createGameStatus(gameStatus);

                //Check left
                while ((columns[i] - j >= 0) && (board.get(rows[i]).charAt(columns[i] - j + 1) == 'X')) {
                    String temp = String.valueOf(board.get(rows[i]).charAt(columns[i] - j));
                    if (temp.equals("#")) {
                        killed = false;
                    }
                    j++;
                }

                //Check right
                j = 1;
                while ((columns[i] + j <= 9) && (board.get(rows[i]).charAt(columns[i] + j - 1) == 'X')) {
                    String temp = String.valueOf(board.get(rows[i]).charAt(columns[i] + j));
                    if (temp.equals("#")) {
                        killed = false;
                    }
                    j++;
                }

                //Check top
                j = 1;
                while ((rows[i] - j >= 0) && (board.get(rows[i] - j + 1).charAt(columns[i]) == 'X')) {
                    String temp = String.valueOf(board.get(rows[i] - j).charAt(columns[i]));
                    if (temp.equals("#")) {
                        killed = false;
                    }
                    j++;
                }

                //Check bottom
                j = 1;
                while ((rows[i] + j <= 9) && (board.get(rows[i] + j - 1).charAt(columns[i]) == 'X')) {
                    String temp = String.valueOf(board.get(rows[i] + j).charAt(columns[i]));
                    if (temp.equals("#")) {
                        killed = false;
                    }
                    j++;
                }

                if (!killed) {
                    salvoDTO.getSalvo().put(salvo.getSalvo()[i], "HIT");

                } else {
                    salvoDTO.getSalvo().put(salvo.getSalvo()[i], "KILL");

                    gameStatus.setRemainingShips(gameStatus.getRemainingShips() - 1);
                    gameStatus.getSelf().setRemainingShips(gameStatus.getRemainingShips());
                    gameStatusService.createGameStatus(gameStatus);

                    if (gameStatus.getRemainingShips() <= 0) {
                        Game gameToSave = gameService.getGameById(gameId);
                        gameToSave.setWon(gameStatus.getOpponent().getPlayerId());
                        gameService.saveGame(gameToSave);

                        GameList attackedPlayerList = gameListService.findById(Long.valueOf(gameStatus.getSelf().getPlayerId()));
                        for (GameListObject gameListObject : attackedPlayerList.getList()) {
                            if (gameListObject.getGameId().equals(gameId)) {
                                gameListObject.setStatus("LOST");
                                gameListObjectService.createGameListObject(gameListObject);
                            }
                        }

                        GameList attackingPlayerList = gameListService.findById(Long.valueOf(gameStatus.getOpponent().getPlayerId()));
                        for (GameListObject gameListObject : attackingPlayerList.getList()) {
                            if (gameListObject.getGameId().equals(gameId)) {
                                gameListObject.setStatus("WON");
                                gameListObjectService.createGameListObject(gameListObject);
                            }
                        }
                    }
                }

            } else {
                StringBuilder boardRow = new StringBuilder(board.get(rows[i]));

                if (!cell.equals("X")) {
                    boardRow.setCharAt(columns[i], 'O');
                    board.set(rows[i], boardRow.toString());
                }

                List<BoardRow> boardRowList = getBoardRows(board);

                attackedPlayerStatus.setBoard(boardRowList);
                playerStatusService.createPlayerStatus(attackedPlayerStatus);

                gameStatus.setSelf(attackedPlayerStatus);
                gameStatusService.createGameStatus(gameStatus);

                salvoDTO.getSalvo().put(salvo.getSalvo()[i], "MISS");
            }
        }
    }

    private List<BoardRow> getBoardRows(List<String> board) {
        List<BoardRow> boardRowList = new ArrayList<>();

        board.iterator().forEachRemaining(s -> {
            BoardRow row = new BoardRow();
            row.setRow(s);
            boardRowService.save(row);
            boardRowList.add(row);
        });

        return boardRowList;
    }
}
