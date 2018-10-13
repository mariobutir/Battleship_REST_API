package mario.battleship.services;

import lombok.extern.slf4j.Slf4j;
import mario.battleship.domain.*;
import mario.battleship.repositories.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GameServiceImpl implements GameService {
    private static final int GRID_COLUMNS = 10;
    private static final int GRID_ROWS = 10;

    private final int BATTLESHIP_SIZE = 4;
    private final int DESTROYER_SIZE = 3;
    private final int SUBMARINE_SIZE = 2;
    private final int PATROL_SIZE = 1;

    private final int NUMBER_OF_BATTLESHIPS = 1;
    private final int NUMBER_OF_DESTROYERS = 2;
    private final int NUMBER_OF_SUBMARINES = 3;
    private final int NUMBER_OF_PATROLS = 4;

    private final int NUMBER_OF_SHIPS = NUMBER_OF_BATTLESHIPS + NUMBER_OF_DESTROYERS + NUMBER_OF_PATROLS + NUMBER_OF_SUBMARINES;

    private final GameRepository gameRepository;
    private final GameStatusService gameStatusService;
    private final PlayerService playerService;
    private final GameListService gameListService;
    private final GameListObjectService gameListObjectService;
    private final PlayerStatusService playerStatusService;
    private final BoardRowServiceImpl boardRowService;

    public GameServiceImpl(GameRepository gameRepository, GameStatusService gameStatusService, PlayerService playerService, GameListService gameListService,
                           GameListObjectService gameListObjectService, PlayerStatusService playerStatusService, BoardRowServiceImpl boardRowService) {
        this.gameRepository = gameRepository;
        this.gameStatusService = gameStatusService;
        this.playerService = playerService;
        this.gameListService = gameListService;
        this.gameListObjectService = gameListObjectService;
        this.playerStatusService = playerStatusService;
        this.boardRowService = boardRowService;
    }

    @Override
    public Game getGameById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    @Override
    public Game saveGame(Game game) {
        return gameRepository.saveAndFlush(game);
    }

    @Override
    public Game createGame(String playerId, String opponentId) {

        Game game = new Game();

        game.setPlayerId(playerId);
        game.setOpponentId(opponentId);

        //Choose playerTurn player
        double random = getRandomIntBetween(0, 100);
        if (random >= 50) {
            game.setPlayerTurn(opponentId);
        } else {
            game.setPlayerTurn(playerId);
        }

        //Create player(challenger) and opponent grid and status
        PlayerStatus playerStatus = createPlayerStatus(playerId, game, "player");
        PlayerStatus opponentStatus = createPlayerStatus(opponentId, game, "opponent");

        //Create initial game status for both players
        GameStatus playerGameStatus = new GameStatus();
        GameStatus opponentGameStatus = new GameStatus();

        gameStatusService.createGameStatus(playerGameStatus);
        gameStatusService.createGameStatus(opponentGameStatus);

        setInitialGameStatus(playerStatus, opponentStatus, playerGameStatus);
        setInitialGameStatus(opponentStatus, playerStatus, opponentGameStatus);

        //Add gameStatus of player and opponent to game and save game
        game.setPlayerGameStatus(playerGameStatus);
        game.setOpponentGameStatus(opponentGameStatus);
        Game createdGame = gameRepository.save(game);

        //Add game to list for both players
        addGameToPlayer(game, playerId, opponentId);
        addGameToPlayer(game, opponentId, playerId);

        opponentGameStatus.setGameId(game.getId().toString());
        playerGameStatus.setGameId(game.getId().toString());

        return createdGame;
    }

    private void setInitialGameStatus(PlayerStatus playerStatus, PlayerStatus opponentStatus, GameStatus playerGameStatus) {
        playerGameStatus.setSelf(playerStatus);
        playerGameStatus.setOpponent(opponentStatus);
        playerGameStatus.setAutopilot(false);
        playerGameStatus.setRemainingShips(NUMBER_OF_SHIPS);
        gameStatusService.createGameStatus(playerGameStatus);
    }

    private PlayerStatus createPlayerStatus(String playerId, Game game, String player) {
        String[][] grid;

        grid = initializeGrid();
        grid = placeShips(grid);
        List<String> stringBoard = Arrays.stream(grid).map(row -> Arrays.stream(row).reduce("", (a, b) -> a + b)).collect(Collectors.toList());

        while (!checkGrid(stringBoard)) {
            grid = initializeGrid();
            grid = placeShips(grid);
            stringBoard = Arrays.stream(grid).map(row -> Arrays.stream(row).reduce("", (a, b) -> a + b)).collect(Collectors.toList());
        }

        List<BoardRow> playerBoard = new ArrayList<>();
        stringBoard.iterator().forEachRemaining(s -> {
            BoardRow boardRow = new BoardRow();
            boardRow.setRow(s);
            boardRowService.save(boardRow);
            playerBoard.add(boardRow);
        });

        PlayerStatus playerStatus = new PlayerStatus();
        playerStatus.setPlayerId(playerId);
        playerStatus.setBoard(playerBoard);
        playerStatus.setRemainingShips(NUMBER_OF_SHIPS);
        playerStatus.setAutopilot(false);
        playerStatusService.createPlayerStatus(playerStatus);

        if (player.equals("player"))
            game.setPlayerGrid(playerBoard);
        else
            game.setOpponentGrid(playerBoard);

        return playerStatus;
    }

    private void addGameToPlayer(Game game, String playerId, String opponentId) {
        Player player = playerService.getPlayerById(Long.valueOf(playerId));
        GameListObject gameListObject = new GameListObject();
        GameList gameList = gameListService.findById(Long.valueOf(playerId));

        if (gameList == null) {
            gameList = new GameList();
            gameList.setId(Long.valueOf(playerId));

        } else {
            gameList = gameListService.findById(Long.valueOf(playerId));
        }

        gameList.setId(Long.valueOf(playerId));
        gameListObject.setGameId(game.getId());
        gameListObject.setOpponentId(Long.valueOf(opponentId));
        gameListObject.setStatus(setGameStatus(game, playerId));

        gameListObjectService.createGameListObject(gameListObject);

        gameList.getList().add(gameListObject);
        GameList savedGameList = gameListService.createGameList(gameList);

        player.setGameList(savedGameList);
        playerService.createPlayer(player);

    }

    private String setGameStatus(Game game, String playerId) {
        if (game.getWon() == null) {
            return "IN_PROGRESS";
        } else if (game.getWon().equals(playerId)) {
            return "WON";
        } else {
            return "LOST";
        }
    }

    private String[][] placeShips(String[][] grid) {
        grid = createShips(grid, BATTLESHIP_SIZE, NUMBER_OF_BATTLESHIPS);
        grid = createShips(grid, DESTROYER_SIZE, NUMBER_OF_DESTROYERS);
        grid = createShips(grid, SUBMARINE_SIZE, NUMBER_OF_SUBMARINES);
        grid = createShips(grid, PATROL_SIZE, NUMBER_OF_PATROLS);
        return grid;
    }

    private String[][] initializeGrid() {
        String[][] initialGrid = new String[GRID_ROWS][GRID_COLUMNS];

        for (int row = 0; row < GRID_ROWS; row++) {
            for (int column = 0; column < GRID_COLUMNS; column++) {
                initialGrid[row][column] = ".";
            }
        }

        return initialGrid;
    }

    private boolean checkGrid(List<String> grid) {
        int counter = 0;
        int cellNumber = NUMBER_OF_BATTLESHIPS * BATTLESHIP_SIZE + NUMBER_OF_SUBMARINES * SUBMARINE_SIZE + NUMBER_OF_PATROLS * PATROL_SIZE + NUMBER_OF_DESTROYERS * DESTROYER_SIZE;

        for(String row : grid){
            counter += StringUtils.countOccurrencesOf(row, "#");
        }

        return (counter == cellNumber);
    }


    private static double getRandomIntBetween(double min, double max) {
        double x = (int) (Math.random() * ((max - min) + 1)) + min;
        return x;
    }

    private static String[][] createShips(String[][] grid, int shipSize, int numberOfShips) {

        while (numberOfShips > 0) {

            if (getRandomIntBetween(0, 100) < 50) {
                String direction = "horizontal";

                int row = (int) getRandomIntBetween(0, GRID_ROWS - 1);
                int column = (int) getRandomIntBetween(0, GRID_COLUMNS - shipSize);

                if (checkShipPlacement(grid, shipSize, direction, row, column)) {
                    for (int i = 0; i < shipSize; i++) {
                        grid[row][column + i] = "#";
                    }

                    numberOfShips -= 1;
                }

            } else {
                String direction = "vertical";

                int row = (int) getRandomIntBetween(0, GRID_ROWS - shipSize);
                int column = (int) getRandomIntBetween(0, GRID_COLUMNS - 1);

                if (checkShipPlacement(grid, shipSize, direction, row, column)) {
                    for (int i = 0; i < shipSize; i++) {
                        grid[row + i][column] = "#";
                    }

                    numberOfShips -= 1;
                }
            }
        }

        return grid;
    }

    private static boolean checkShipPlacement(String[][] grid, int shipSize, String direction, int row, int column) {

        switch (direction) {

            case "horizontal": {

                //check left of ship
                if ((column - 1) >= 0) {

                    //center left
                    if (grid[row][column - 1].equals("#")) {
                        return false;
                    }

                    //top left
                    if ((row - 1) >= 0) {
                        if (grid[row - 1][column - 1].equals("#")) {
                            return false;
                        }
                    }

                    //bottom left
                    if ((row + 1) <= 9) {
                        if (grid[row + 1][column - 1].equals("#")) {
                            return false;
                        }
                    }
                }

                //check right of ship
                if ((column + shipSize) <= 9) {

                    //center right
                    if (grid[row][column + shipSize].equals("#")) {
                        return false;
                    }

                    //top right
                    if ((row - 1) >= 0) {
                        if (grid[row - 1][column + shipSize].equals("#")) {
                            return false;
                        }
                    }

                    //bottom right
                    if ((row + 1) <= 9) {
                        if (grid[row + 1][column + shipSize].equals("#")) {
                            return false;
                        }
                    }
                }

                //check top and bottom side of ship
                for (int i = 0; i < shipSize; i++) {

                    //top
                    if ((row - 1) >= 0) {
                        if (grid[row - 1][column + i].equals("#")) {
                            return false;
                        }
                    }

                    //bottom
                    if ((row + 1) <= 9) {
                        if (grid[row + 1][column + i].equals("#")) {
                            return false;
                        }
                    }
                }

                return true;
            }

            case "vertical": {

                //check top side of ship
                if ((row - 1) >= 0) {

                    //center top
                    if (grid[row - 1][column].equals("#")) {
                        return false;
                    }

                    //left top
                    if ((column - 1) >= 0) {
                        if (grid[row - 1][column - 1].equals("#")) {
                            return false;
                        }
                    }

                    //right top
                    if ((column + 1) <= 9) {
                        if (grid[row - 1][column + 1].equals("#")) {
                            return false;
                        }
                    }
                }

                //check bottom side of ship
                if ((row + shipSize) <= 9) {

                    //bottom center
                    if (grid[row + shipSize][column].equals("#")) {
                        return false;
                    }

                    //bottom left
                    if ((column - 1) >= 0) {
                        if (grid[row + shipSize][column - 1].equals("#")) {
                            return false;
                        }
                    }

                    //bottom right
                    if ((column + 1) <= 9) {
                        if (grid[row + shipSize][column + 1].equals("#")) {
                            return false;
                        }
                    }
                }

                //check left and right side of ship
                for (int i = 0; i < shipSize; i++) {

                    //left
                    if ((column - 1) >= 0) {
                        if (grid[row + i][column - 1].equals("#")) {
                            return false;
                        }
                    }

                    //right
                    if ((column + 1) <= 9) {
                        if (grid[row + i][column + 1].equals("#")) {
                            return false;
                        }
                    }
                }

                return true;
            }

            default:
                return false;
        }
    }
}
