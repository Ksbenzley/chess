package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exceptions.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Handler;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameServerMessage;
import server.*;
import websocket.messages.NotificationServerMessage;
import java.io.IOException;
import java.util.ArrayList;

@WebSocket
public class WebSocketHandler {

    private final AuthDAO auth;
    private final GameDAO game;
    ConnectionManager manager = new ConnectionManager();

    public WebSocketHandler(AuthDAO auth, GameDAO game) {
        this.auth = auth;
        this.game = game;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, IOException {
        Gson serializer = new Gson();
        UserGameCommand command = serializer.fromJson(message, UserGameCommand.class);
        int gameID = command.getGameID();
        String userName = command.getUsername();

        switch(command.getCommandType()){
            case CONNECT:
                manager.add(gameID, userName, session);
                LoadGameServerMessage msg = new LoadGameServerMessage(GameManager.getGame(gameID));
                manager.broadcastToOnly(gameID, session, msg);
                if(command.getIsObserver()){
                    NotificationServerMessage notif = new NotificationServerMessage(userName + " joined the game as an observer");
                    manager.broadcastToAllExcept(gameID, session, notif);
                }else{
                    NotificationServerMessage notif = new NotificationServerMessage(userName + " joined the game as " + command.getColor());
                    manager.broadcastToAllExcept(gameID, session, notif);
                }
                break;

            case LEAVE:
                NotificationServerMessage leftGame = new NotificationServerMessage(userName + " has left the game");
                manager.broadcastToAllExcept(gameID, session, leftGame);
                manager.remove(gameID, session);
                game.setPlayerColor(command.getColor(), gameID, null);
                break;

            case RESIGN:
                NotificationServerMessage resigned = new NotificationServerMessage(userName + " has resigned");
                manager.broadcastToAll(gameID, resigned);
                game.setPlayerColor(command.getColor(), gameID, null);
                break;

            case MAKE_MOVE:

                break;
        }
    }
}
