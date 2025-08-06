package websocket;

import com.google.gson.Gson;
import exceptions.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Handler;
import websocket.commands.UserGameCommand;

import java.util.ArrayList;

@WebSocket
public class WebSocketHandler {

    ConnectionManager manager = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException {
        Gson serializer = new Gson();
        UserGameCommand command = serializer.fromJson(message, UserGameCommand.class);
        int gameID = command.getGameID();
        String userName = command.getUsername();

        switch(command.getCommandType()){
            case CONNECT:
                manager.add(gameID, userName, session);
                manager.broadcast();
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        ArrayList<GameData> list = Handler.getDAO().getList();
        for(GameData game : list){
            if(game.gameID().equals(gameID)){
                return game;
            }
        }
        return null;
    }

}
