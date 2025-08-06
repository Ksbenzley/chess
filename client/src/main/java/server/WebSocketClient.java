package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import ui.Board;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClient extends Endpoint{

    public Session session;
    public String userName;
    AuthDAO authDAO;

    public WebSocketClient(String authToken, int gameID) throws BadRequestException, URISyntaxException, DeploymentException, IOException, DataAccessException {
        userName = authDAO.getUser(authToken);
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        var serializer = new Gson();
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, userName);
        send(serializer.toJson(command));
    }

    public void send(String msg) throws BadRequestException, IOException {
        this.session.getBasicRemote().sendText(msg);
    }

    @OnMessage
    public void onMessage(String message){
        var serializer = new Gson();
        var command = serializer.fromJson(message, ServerMessage.class);
        System.out.println(message + "\n");

        if(command.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){

        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
