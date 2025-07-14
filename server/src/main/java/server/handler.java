package server;
import service.userService;
import com.google.gson.Gson;
import spark.*;

public class handler implements Route{

    @Override
    public Object handle(Request req, Response res) throws Exception{

        return res;
    }

    public Object register(Request request, Response response) {
        //serializing from Json to Java
        String body = request.body();
        var serializer = new Gson();
        var data = serializer.fromJson(body, RegisterRequest.class);

        userService.registerUser(data.username, data.password, data.eMail);

        return response;

    }

    private static class RegisterRequest {
        String username;
        String password;
        String eMail;
    }

}
