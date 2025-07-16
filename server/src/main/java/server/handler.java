package server;
import dataaccess.*;
import service.*;
import com.google.gson.Gson;
import spark.*;

public class handler{
    static MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    static MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    static MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

    public static Object listGames(Request request, Response response){
        var serializer = new Gson();
        try{
            String body = request.headers("authorization");
            response.status(200);

            String str = String.join(", ", GameService.listGames(body, memoryAuthDAO, memoryGameDAO));
            listGamesResponse result = new listGamesResponse(str);

            response.status(200);
            return serializer.toJson(result);

        }catch(NotAuthorizedException x){
            response.status(401);
            return new ErrorResponse(x.getMessage());
        }
    }

    public static Object createGame(Request request, Response response){
        var serializer = new Gson();

        try{
            String authToken = request.headers("authorization");
            String body = request.body();
            var data = serializer.fromJson(body, CreateGameRequest.class);

            int gameID = GameService.createGame(authToken, data.gameName, memoryAuthDAO, memoryGameDAO);
            response.status(200);

            String str = String.valueOf(gameID);
            createGameResponse result = new createGameResponse(str);

            return serializer.toJson(result);
        }catch(NotAuthorizedException x){
            response.status(401);
            return new ErrorResponse(x.getMessage());
        }
    }

    public static Object logout(Request request, Response response){
        var serializer = new Gson();

        try{
            String body = request.body();
            var data = serializer.fromJson(body, LogoutRequest.class);

            DatabaseService.logout(data.authToken, memoryAuthDAO);
            response.status(200);
            return "{}";
        }catch(NotAuthorizedException x){
            response.status(401);
            return new ErrorResponse(x.getMessage());
        }
    }

    public static Object clear(Request request, Response response){
        DatabaseService.clear(memoryUserDAO, memoryAuthDAO);
        response.status(200);
        return "{}";
    }

    public static Object login(Request request, Response response){
       var serializer = new Gson();

        try{
            String body = request.body();
            var data = serializer.fromJson(body, LoginRequest.class);

            String authToken = userService.loginUser(data.username, data.password, memoryUserDAO, memoryAuthDAO);

            response.status(200);
            RegisterResponse result = new RegisterResponse(data.username, authToken);
            return serializer.toJson(result);
        }catch(UserDoesNotExistException | PasswordsDontMatchException x){
            response.status(403);
            return serializer.toJson(new ErrorResponse(x.getMessage()));
        }catch(NotAuthorizedException x){
            response.status(401);
            return serializer.toJson(new ErrorResponse(x.getMessage()));
        }
    }

    public static Object register(Request request, Response response) {

        var serializer = new Gson();

        try {
            String body = request.body();
            var data = serializer.fromJson(body, RegisterRequest.class);

            //gets the authToken and registers the user
            String authToken = userService.registerUser(data.username, data.password, data.email, memoryUserDAO, memoryAuthDAO);
            RegisterResponse result = new RegisterResponse(data.username, authToken);

            response.status(200);
            //response.type("application/JSON");
            return serializer.toJson(result);
        }catch (AlreadyTakenException x){
            response.status(403);
            return serializer.toJson(new ErrorResponse(x.getMessage()));
        }
    }

    private static class LogoutRequest {
        String authToken;
    }

    private static class CreateGameRequest {
        String gameName;
    }

    private static class RegisterRequest {
        String username;
        String password;
        String email;
    }

    private static class listGamesResponse {
        String list;

        listGamesResponse(String list){
            this.list = list;
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

    private static class createGameResponse {
        String gameID;

        createGameResponse(String gameID){
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
