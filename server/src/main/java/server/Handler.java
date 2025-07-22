package server;
import dataaccess.*;
import model.*;
import service.*;
import com.google.gson.Gson;
import spark.*;

import java.util.List;

public class Handler {

    private static UserDAO UserDAO;
    private static GameDAO GameDAO;
    private static AuthDAO AuthDAO;
    private static Boolean created = false;

    private static void createDB(){
        if(!created){
            try{
                UserDAO = new SQLUserDAO();
                GameDAO = new SQLGameDAO();
                AuthDAO = new SQLAuthDAO();
            }catch(Exception x){
//                GameDAO = new MemoryGameDAO();
//                UserDAO = new MemoryUserDAO();
//                AuthDAO = new MemoryAuthDAO();
            }
            created = true;
        }
    }

    public static Object joinGame(Request request, Response response) throws DataAccessException{
        createDB();
        response.type("application/json");
        var serializer = new Gson();
        String authToken = request.headers("authorization");
        String body = request.body();
        var data = serializer.fromJson(body, JoinGameRequest.class);

        try{
            GameService.joinGame(authToken, data.playerColor, data.gameID, AuthDAO, GameDAO);
            response.status(200);
            return "{}";
        }catch(BadRequestException x){
            response.status(400);
            return serializer.toJson(new ErrorResponse(x.getMessage()));
        }catch(AlreadyTakenException x){
            response.status(403);
            return serializer.toJson(new ErrorResponse(x.getMessage()));
        }catch(NotAuthorizedException x){
            response.status(401);
            return serializer.toJson(new ErrorResponse(x.getMessage()));
        }
    }

    public static Object listGames(Request request, Response response) throws DataAccessException{
        createDB();
        response.type("application/json");
        var serializer = new Gson();
        try{
            String body = request.headers("authorization");

            List<GameData> games = GameService.listGames(body, AuthDAO, GameDAO);
            ListGamesResponse result = new ListGamesResponse(games);

            response.status(200);
            return serializer.toJson(result);

        }catch(NotAuthorizedException x){
            response.status(401);
            return serializer.toJson(new ErrorResponse(x.getMessage()));
        }
    }

    public static Object createGame(Request request, Response response) throws DataAccessException{
        createDB();
        response.type("application/json");
        var serializer = new Gson();

        try{
            String authToken = request.headers("authorization");
            String body = request.body();
            var data = serializer.fromJson(body, CreateGameRequest.class);

            int gameID = GameService.createGame(authToken, data.gameName, AuthDAO, GameDAO);

            response.status(200);
            CreateGameResponse result = new CreateGameResponse(gameID);
            return serializer.toJson(result);
        }catch(NotAuthorizedException x){
            response.status(401);
            return serializer.toJson(new ErrorResponse(x.getMessage()));
        }catch(BadRequestException x){
            response.status(400);
            return serializer.toJson(new ErrorResponse(x.getMessage()));
        }
    }

    public static Object logout(Request request, Response response) throws DataAccessException{
        createDB();
        response.type("application/json");
        var serializer = new Gson();
        String authToken = request.headers("authorization");
        if(authToken == null){
            response.status(401);
            return serializer.toJson(new ErrorResponse("Error: bad request"));
        }
        try{
            DatabaseService.logout(authToken, AuthDAO);
            response.status(200);
            return "{}";
        }catch(NotAuthorizedException x){
            response.status(401);
            return serializer.toJson(new ErrorResponse(x.getMessage()));
        }catch(BadRequestException x){
            response.status(400);
            return serializer.toJson(new ErrorResponse(x.getMessage()));
        }
    }

    public static Object clear(Request request, Response response) throws DataAccessException{
        createDB();
        DatabaseService.clear(UserDAO, AuthDAO, GameDAO);
        response.status(200);
        return "{}";
    }

    public static Object login(Request request, Response response) throws DataAccessException{
        createDB();
        response.type("application/json");
        var serializer = new Gson();
        var data = serializer.fromJson(request.body(), LoginRequest.class);
        try{
            String authToken = UserService.loginUser(data.username, data.password, UserDAO, AuthDAO);

            response.status(200);
            RegisterResponse result = new RegisterResponse(data.username, authToken);
            return serializer.toJson(result);
        }catch(BadRequestException x){
            response.status(400);
            return serializer.toJson(new ErrorResponse(x.getMessage()));
        }catch(NotAuthorizedException x){
            response.status(401);
            return serializer.toJson(new ErrorResponse(x.getMessage()));
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
            String authToken = UserService.registerUser(data.username, data.password, data.email, UserDAO, AuthDAO);
            RegisterResponse result = new RegisterResponse(data.username, authToken);

            response.status(200);
            return serializer.toJson(result);
        }catch (AlreadyTakenException x){
            response.status(403);
            return serializer.toJson(new ErrorResponse(x.getMessage()));
        }catch (BadRequestException x){
            response.status(400);
            return serializer.toJson(new ErrorResponse(x.getMessage()));
        }
    }

    private static class JoinGameRequest {
        String playerColor;
        int gameID;
    }

    private static class CreateGameRequest {
        String gameName;
    }

    private static class RegisterRequest {
        String username;
        String password;
        String email;
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

    private static class LoginRequest{
        String username;
        String password;
    }

    private static class ErrorResponse {
        String message;

        ErrorResponse(String message) {
            this.message = message;
        }
    }
}
