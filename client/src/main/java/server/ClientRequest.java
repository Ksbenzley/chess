package server;

import chess.*;
import exceptions.DataAccessException;
import exceptions.NotAuthorizedException;
import ui.*;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import model.GameData;
import model.UserData;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientRequest {
    private final ServerFacade server;
    private Boolean inGameplay = false;
    private final String serverUrl;
    private String authToken;
    private final HashMap<Integer, GameData> gameList = new HashMap<>();
    private State state = State.SIGNEDOUT;
    private int gameID;
    private Boolean whitePlayer;

    public HashMap<Integer, GameData> getGameList(){
        return gameList;
    }

    public ClientRequest(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String evaluate(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if(state == State.SIGNEDIN) {
                if(inGameplay){
                    return switch (cmd) {
                        case "redraw" -> redraw();
                        case "leave" -> leaveGame();
                        case "move" -> makeMove(params);
                        case "resign" -> resign();
                        //case "show" -> showMoves(params);
                        default -> help();
                    };
                }
                return switch (cmd) {
                    case "logout" -> logout();
                    case "create" -> createGame(params);
                    case "list" -> listGames(params);
                    case "play" -> playGame(params);
                    case "observe" -> observeGame(params);
                    case "quit" -> "quit";
                    default -> help();
                };
            }else{
                return switch (cmd) {
                    case "login" -> signIn(params);
                    case "register" -> register(params);
                    case "quit" -> "quit";
                    default -> help();
                };
            }
        } catch (BadRequestException | NotAuthorizedException | AlreadyTakenException | DeploymentException |
        URISyntaxException | IOException | DataAccessException x) {
            return x.getMessage();
        }
    }

    public String leaveGame() throws DeploymentException, IOException, URISyntaxException, DataAccessException {
        server.leaveGame(gameID, "WHITE");
        inGameplay = false;
        return "";
    }

    public String resign() throws DeploymentException, URISyntaxException, IOException, DataAccessException {
        if (whitePlayer) {
            server.resign(gameID, "WHITE");
        }else{
            server.resign(gameID, "BLACK");
        }
        return "";
    }

    public String redraw(){
        ChessBoard board = GameManager.getGame(gameID).getBoard();
        Board makeBoard = new Board();
        if(whitePlayer){
            makeBoard.run("white", board);
        }else {
            makeBoard.run("black", board);
        }
        return "";
    }

    public String makeMove(String... params){
        if (params.length >= 2) {
            try {
                ChessPosition start = getPos(params[0]);
                ChessPosition end = getPos(params[1]);
                server.makeMove(start, end, gameID, "WHITE");
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        }else{
            throw new BadRequestException("Expected: <START POSITION> <END POSITION>");
        }
        return "";
    }

    public ChessPosition getPos(String position){
        char rowChar = position.charAt(1);
        char colChar = position.charAt(0);
        int col = 0;
        switch (colChar) {
            case 'a' -> col = 1;
            case 'b' -> col = 2;
            case 'c' -> col = 3;
            case 'd' -> col = 4;
            case 'e' -> col = 5;
            case 'f' -> col = 6;
            case 'g' -> col = 7;
            case 'h' -> col = 8;
        }

        int row = rowChar - '0';
        return new ChessPosition(row, col);
    }

    public String observeGame(String... params) throws BadRequestException {
        int gameNum;
        if (params.length >= 1){
            try {
                gameNum = Integer.parseInt(params[0]);
            }catch(NumberFormatException e){
                throw new BadRequestException("Expected: <GAME NUMBER>" + "\n");
            }

            if (!gameList.containsKey(gameNum)) {
                throw new BadRequestException("Error: game number out of range" + "\n");
            }

            server.observeGame(gameNum, authToken, gameList.get(gameNum).gameID(), "WHITE");
            System.out.println("Now observing: " + gameList.get(gameNum).gameName() + "\n");
            return "";
        }else{
            throw new BadRequestException("Expected: <GAME NUMBER>" + "\n");
        }
    }

    public String playGame(String... params) throws BadRequestException, NumberFormatException {
        loadGames();
        if (params.length >= 2) {
            int gameNum;
            try {
                gameNum = Integer.parseInt(params[0]);
                if (gameList.size() < gameNum || gameNum <= 0){
                    throw new BadRequestException("Error: game number out of range" + "\n");
                }
            }catch (NumberFormatException e) {
                throw new BadRequestException("Expected: <GAME NUMBER> <WHITE|BLACK>" + "\n");
            }
            String color = params[1];
            if(color.equalsIgnoreCase("white")){
                whitePlayer = true;
            }else{
                whitePlayer = false;
            }

            this.gameID = gameList.get(gameNum).gameID();
            server.playGame(authToken, gameList.get(gameNum).gameID(), color);

            inGameplay = true;
            System.out.print("Now playing in: " + gameList.get(gameNum).gameName() + "\n");
            return "";
        }else{
            throw new BadRequestException("Expected: <GAME NUMBER> <WHITE|BLACK>" + "\n");
        }
    }

    public void loadGames() throws BadRequestException {
        ArrayList<GameData> games;
        int i = 1;
        games = server.listGames();
        for(GameData game: games){
            gameList.put(i, game);
            i++;
        }
    }

    public String listGames(String... params) throws BadRequestException{
        ArrayList<GameData> games;
        int i = 1;
        games = server.listGames();
        for(GameData game : games){
            System.out.println(i + ". " + game.gameName());
            System.out.println("    White Player: " + game.whiteUsername());
            System.out.println("    Black Player: " + game.blackUsername());
            gameList.put(i, game);
            i++;
        }
        return "";
    }

    public String createGame(String... params) throws BadRequestException {
        if (params.length >= 1) {
            String name = params[0];
            GameData gameData = server.createGame(name);
            this.gameID = gameData.gameID();
            System.out.println("game created: " + name);
            loadGames();
            return "";
        }else{
            throw new BadRequestException("Expected: <GAME NAME>");
        }
    }

    public String logout() throws BadRequestException {
        state = State.SIGNEDOUT;
        server.logout();
        authToken = null;
        System.out.println("You have successfully signed out");
        return "";
    }

    public String register(String... params) throws AlreadyTakenException {
        try {
            if (params.length >= 3) {

                String name = params[0];
                String pass = params[1];
                String email = params[2];

                server.register(new UserData(name, pass, email));

                authToken = server.login(new UserData(name, pass, null)).authToken();
                state = State.SIGNEDIN;
                loadGames();
                return "You are signed in as " + name + "\n";
            } else {
                throw new BadRequestException("Expected: bad input");
            }
        } catch (AlreadyTakenException e) {
            throw e;
        }
    }

    public String signIn(String... params) throws BadRequestException, NotAuthorizedException {
        if (params.length >= 2) {
            String name = params[0];
            String pass = params[1];

            authToken = server.login(new UserData(name, pass, null)).authToken();
            loadGames();
            state = State.SIGNEDIN;
            return "You are signed in as: " + name + "\n";
        } else {
            throw new BadRequestException("Expected: <USERNAME> <PASSWORD>");
        }
    }

    public String help(){
        if (state == State.SIGNEDIN){
            if(inGameplay == false) {
                return """
                        play <GAME NUMBER> <WHITE|BLACK> - to play chess
                        create <GAME NAME> - to create a new game
                        observe <GAME NUMBER> - to watch a game
                        list - show all games
                        logout - to switch accounts
                        quit - end the session
                        help - show commands
                        """;
            }else{
                return """
                        move <START POSITION> <END POSITION>- make a chess move
                        resign - resign from the game
                        redraw - update the chessboard
                        leave - leave the game
                        highlight - show legal moves
                        help - show commands
                        """;
            }
        }else {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - end the session
                    help - show commands
                    """;
        }
    }
}
