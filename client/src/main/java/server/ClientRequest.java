package server;

import chess.ChessBoard;
import exceptions.NotAuthorizedException;
import ui.*;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientRequest {
    private final ServerFacade server;
    private final String serverUrl;
    private String authToken;
    private final HashMap<Integer, GameData> gameList = new HashMap<>();
    private State state = State.SIGNEDOUT;

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
            return switch (cmd) {
                case "login" -> signIn(params);
                case "register" -> register(params);
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "play" -> playGame(params);
                case "observe" -> observeGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (BadRequestException | NotAuthorizedException | AlreadyTakenException x) {
            return x.getMessage();
        }
    }

    public String observeGame(String... params) throws BadRequestException {
        int gameID;
        if (params.length >= 1){
            try {
                gameID = Integer.parseInt(params[0]);
            }catch(NumberFormatException e){
                throw new BadRequestException("Expected: <GAME NUMBER>" + "\n");
            }

            if (!gameList.containsKey(gameID)) {
                throw new BadRequestException("Error: game number out of range" + "\n");
            }
//            ChessBoard board = new ChessBoard();
//            board.resetBoard();
            Board makeBoard = new Board();
            makeBoard.run("WHITE");
            System.out.println("Now observing: " + gameList.get(gameID).gameName() + "\n");
            return "";
        }else{
            throw new BadRequestException("Expected: <GAME NUMBER>" + "\n");
        }
    }

    public String playGame(String... params) throws BadRequestException, NumberFormatException {
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

            server.playGame(gameList.get(gameNum).gameID(), color);
//            ChessBoard board = new ChessBoard();
//            board.resetBoard();
            Board board = new Board();
            board.run(color);
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
            server.createGame(name);
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
        }catch (AlreadyTakenException e){
            throw e;
        }
    }

    public String signIn(String... params) throws BadRequestException {
        if (params.length >= 2) {
            String name = params[0];
            String pass = params[1];

            authToken = server.login(new UserData(name, pass, null)).authToken();
            loadGames();
            state = State.SIGNEDIN;
            return "You are signed in as: " + name + "\n";
        }else{
            throw new BadRequestException("Expected: <USERNAME> <PASSWORD>");
        }
    }

    public String help(){
        if (state == State.SIGNEDOUT){
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - end the session
                    help - show commands
                    """;
        }else{
            return """
                    play game <GAME NUMBER> <WHITE|BLACK> - to play chess
                    create game <GAME NAME> - to create a new game
                    observe game <GAME NUMBER> - to watch a game
                    list games - show all games
                    logout - to switch accounts
                    quit - end the session
                    help - show commands
                    """;
        }

    }
}
