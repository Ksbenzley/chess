package server;
import dataaccess.*;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import exceptions.NotAuthorizedException;
import model.*;
import service.*;
import com.google.gson.Gson;
import spark.*;
import requests.*;

import java.util.List;
import java.util.Map;

public class Handler {

    private static UserDAO userDAO;
    private static GameDAO gameDAO;
    private static AuthDAO authDAO;
    private static Boolean created = false;

    public Handler(){
        createDB();
    }

    private static void createDB(){
        if(!created){
            try{
                userDAO = new SQLUserDAO();
                gameDAO = new SQLGameDAO();
                authDAO = new SQLAuthDAO();
            }catch(Exception x){
                gameDAO = new MemoryGameDAO();
                userDAO = new MemoryUserDAO();
                authDAO = new MemoryAuthDAO();
            }
            created = true;
        }
    }

    public static AuthDAO getAuthDAO(){
        return authDAO;
    }

    public static GameDAO getGameDAO(){
        return gameDAO;
    }

    public static Object joinGame(Request request, Response response) throws DataAccessException {
        createDB();
        response.type("application/json");
        var serializer = new Gson();
        String authToken = request.headers("authorization");
        String body = request.body();
        var data = serializer.fromJson(body, JoinRequest.class);

        try{
            GameService.joinGame(authToken, data.playerColor(), data.gameID(), authDAO, gameDAO);
            response.status(200);
            return "{}";
        }catch(BadRequestException x){
            response.status(400);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }catch(AlreadyTakenException x){
            response.status(403);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }catch(NotAuthorizedException x) {
            response.status(401);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }catch(DataAccessException x){
            response.status(500);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }
    }

    public static Object listGames(Request request, Response response) throws DataAccessException{
        createDB();
        response.type("application/json");
        var serializer = new Gson();
        try{
            String body = request.headers("authorization");

            List<GameData> games = GameService.listGames(body, authDAO, gameDAO);
            ListGamesResponse result = new ListGamesResponse(games);

            response.status(200);
            return serializer.toJson(result);

        }catch(NotAuthorizedException x){
            response.status(401);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }catch(DataAccessException x){
            response.status(500);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }
    }

    public static Object createGame(Request request, Response response) throws DataAccessException{
        createDB();
        response.type("application/json");
        var serializer = new Gson();

        try{
            String authToken = request.headers("authorization");
            String body = request.body();
            var data = serializer.fromJson(body, CreateRequest.class);

            int gameID = GameService.createGame(authToken, data.gameName(), authDAO, gameDAO);

            response.status(200);
            CreateGameResponse result = new CreateGameResponse(gameID);
            return serializer.toJson(result);
        }catch(NotAuthorizedException x){
            response.status(401);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }catch(BadRequestException x){
            response.status(400);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }catch(DataAccessException x){
            response.status(500);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }
    }

    public static Object logout(Request request, Response response) throws DataAccessException, NotAuthorizedException{
        createDB();
        response.type("application/json");
        var serializer = new Gson();
        String authToken = request.headers("authorization");
        if(authToken == null){
            response.status(401);
            return serializer.toJson(new ErrorResponse("Error: bad request"));
        }
        try{
            DatabaseService.logout(authToken, authDAO);
            response.status(200);
            return "{}";
        }catch(NotAuthorizedException x){
            response.status(401);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }catch(BadRequestException x){
            response.status(400);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }catch(DataAccessException x){
            response.status(500);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }
    }

    public static Object clear(Request request, Response response) throws DataAccessException{
        createDB();
        var serializer = new Gson();
        try {
            DatabaseService.clear(userDAO, authDAO, gameDAO);
            response.status(200);
            return "{}";
        }catch(DataAccessException x){
            response.status(500);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }
    }

    public static Object login(Request request, Response response) throws DataAccessException{
        createDB();
        response.type("application/json");
        var serializer = new Gson();
        var data = serializer.fromJson(request.body(), LoginRequest.class);
        try{
            String authToken = UserService.loginUser(data.username(), data.password(), userDAO, authDAO);

            response.status(200);
            RegisterResponse result = new RegisterResponse(data.username(), authToken);
            return serializer.toJson(result);
        }catch(BadRequestException x){
            response.status(400);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }catch(NotAuthorizedException x){
            response.status(401);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }catch(DataAccessException x){
            response.status(500);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }
    }

    public static Object register(Request request, Response response) throws DataAccessException{
        createDB();
        response.type("application/json");
        var serializer = new Gson();

        try {
            String body = request.body();
            var data = serializer.fromJson(body, RegisterRequest.class);

            //gets the authToken and registers the user
            String authToken = UserService.registerUser(data.username(), data.password(), data.email(), userDAO, authDAO);
            RegisterResponse result = new RegisterResponse(data.username(), authToken);

            response.status(200);
            return serializer.toJson(result);
        }catch (AlreadyTakenException x){
            response.status(403);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }catch (BadRequestException x){
            response.status(400);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }catch(DataAccessException x){
            response.status(500);
            return serializer.toJson(Map.of("message", x.getMessage()));
        }
    }

    private static class ListGamesResponse {
        private List<GameData> games;

        public ListGamesResponse(List<GameData> games){
            this.games = games;
        }
    }

    private static class RegisterResponse {
        String username;
        String authToken;

        RegisterResponse(String username, String authToken) {
            this.username = username;
            this.authToken = authToken;
        }
    }

    private static class CreateGameResponse {
        int gameID;

        CreateGameResponse(int gameID){
            this.gameID = gameID;
        }
    }

    private static class ErrorResponse {
        String message;

        ErrorResponse(String message) {
            this.message = message;
        }
    }
}
