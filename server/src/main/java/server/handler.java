package server;
import dataaccess.AlreadyTakenException;
import dataaccess.NotAuthorizedException;
import dataaccess.PasswordsDontMatchException;
import dataaccess.UserDoesNotExistException;
import service.userService;
import com.google.gson.Gson;
import spark.*;

public class handler{

    public static Object login(Request request, Response response){
       var serializer = new Gson();

        try{
            String body = request.body();
            var data = serializer.fromJson(body, LoginRequest.class);

            String authToken = userService.loginUser(data.username, data.password);

            response.status(200);
            RegisterResponse result = new RegisterResponse(data.username, authToken);
            return serializer.toJson(result);
        }catch(UserDoesNotExistException | PasswordsDontMatchException x){
            response.status(400);
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
            String authToken = userService.registerUser(data.username, data.password, data.email);
            RegisterResponse result = new RegisterResponse(data.username, authToken);

            response.status(200);
            //response.type("application/JSON");
            return serializer.toJson(result);
        }catch (AlreadyTakenException x){
            response.status(403);
            return serializer.toJson(new ErrorResponse(x.getMessage()));
        }
    }

    private static class RegisterRequest {
        String username;
        String password;
        String email;
    }

    private static class RegisterResponse {
        String username;
        String authToken;

        RegisterResponse(String username, String authToken) {
            this.username = username;
            this.authToken = authToken;
        }
    }

    private static class LoginRequest{
        String username;
        String password;
        String authToken;
    }

    private static class ErrorResponse {
        String message;

        ErrorResponse(String message) {
            this.message = message;
        }
    }

}
