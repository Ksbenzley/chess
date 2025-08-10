package server;

import chess.ChessBoard;
import chess.ChessMove;
import com.google.gson.JsonObject;
import exceptions.*;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import requests.*;

import javax.websocket.DeploymentException;
import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class ServerFacade {

    public final String serverUrl;
    public String authToken;

    public ServerFacade(String url) {
        this.serverUrl = url;
    }

    public AuthData login(UserData userData) throws NotAuthorizedException{
        var path = "/session";
        LoginRequest request = new LoginRequest(userData.username(), userData.password());
        AuthData data = this.makeRequest("POST", path, request, AuthData.class);
        authToken = data.authToken();
        return data;
    }

    public void leaveGame(int gameID, String color) throws IOException, DeploymentException, URISyntaxException, DataAccessException {
        WebSocketClient webSocket = new WebSocketClient(authToken, gameID, color, false);
        webSocket.sendLeave();
    }

    public void resign(int gameID, String color) throws DeploymentException, URISyntaxException, IOException, DataAccessException {
        WebSocketClient webSocket = new WebSocketClient(authToken, gameID, color, false);
        webSocket.sendResign();
    }

    public void makeMove(ChessMove move, int gameID, String color){
        try {
            WebSocketClient webSocket = new WebSocketClient(authToken, gameID, color, true);
            webSocket.sendMakeMove(move);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public AuthData register(UserData userData) throws AlreadyTakenException {
        var path = "/user";
        RegisterRequest request = new RegisterRequest(userData.username(), userData.password(), userData.email());
        AuthData data = this.makeRequest("POST", path, request, AuthData.class);
        authToken = data.authToken();
        return data;
    }

    public String logout() throws BadRequestException{
        var path = "/session";
        return this.makeRequest("DELETE", path, null, null);
    }

    public GameData createGame(String name) throws BadRequestException {
        var path = "/game";
        CreateRequest newGame = new CreateRequest(name);
        return this.makeRequest("POST", path, newGame, GameData.class);
    }

    private class ListGamesResponse {
        private ArrayList<GameData> games;
        public ArrayList<GameData> getGames() {
            return games;
        }
    }

    public ArrayList listGames() throws BadRequestException {
        var path = "/game";
        ListGamesResponse response = this.makeRequest("GET", path, null, ListGamesResponse.class);
        return response.getGames();
    }

    public void observeGame(int gameNum, String authToken, int gameID, String color) throws BadRequestException {
        var path = "/game";
        ArrayList games;
        Object game;
        games = listGames();
        game = games.get(gameNum);
        this.makeRequest("PUT", path, game, null);

        try {
            WebSocketClient webSocket = new WebSocketClient(authToken, gameID, color, true);
            webSocket.sendConnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void playGame(String authToken, int gameID, String color) throws BadRequestException {
        var path = "/game";
        JoinRequest request = new JoinRequest(color.toUpperCase(), gameID);
        this.makeRequest("PUT", path, request, null);

        try {
            WebSocketClient webSocket = new WebSocketClient(authToken, gameID, color, false);
            webSocket.sendConnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass)
            throws BadRequestException, NotAuthorizedException, AlreadyTakenException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.addRequestProperty("authorization", authToken);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            if (responseClass != null) {
                return readBody(http, responseClass);
            } else {
                return null;
            }
        } catch (BadRequestException ex) {
            throw ex;
        } catch (AlreadyTakenException ex) {
            throw new AlreadyTakenException(ex.getMessage());
        } catch (NotAuthorizedException ex) {
            throw new NotAuthorizedException(ex.getMessage());
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http)
            throws IOException, BadRequestException, NotAuthorizedException, AlreadyTakenException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            String error = "Unexpected error";
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    InputStreamReader reader = new InputStreamReader(respErr);
                    JsonObject json = new Gson().fromJson(reader, JsonObject.class);
                    if (json.has("message")) {
                        error = json.get("message").getAsString();
                    }
                }
            }
            switch (status){
                case 400 -> {
                    if(error.toLowerCase().contains("username already taken\n")){
                        throw new AlreadyTakenException(error + "\n");
                    }else{
                        throw new BadRequestException(error + "\n");
                    }
                }
                case 401 -> {
                    throw new NotAuthorizedException("Error: Not Authorized\n");
                }
                case 403 -> throw new AlreadyTakenException(error + "\n");
                default -> throw new BadRequestException(error + "\n");
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
