package server;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import ui.Board;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameServerMessage;
import websocket.messages.ServerMessage;
import chess.ChessGame.*;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClient extends Endpoint{

    public Session session;
    public String userName;
    //AuthDAO authDAO;
    public TeamColor teamColor;
    private Gson serializer = new Gson();
    private final String authToken;
    private int gameID;
    private String color;
    private Boolean isObserver;

    public WebSocketClient(String authToken, int gameID, String color, Boolean isObserver, String username)
            throws BadRequestException, URISyntaxException, DeploymentException, IOException, DataAccessException {

        this.authToken = authToken;
        this.gameID = gameID;
        this.color = color;
        this.isObserver = isObserver;
        this.userName = userName;

        if(color.equalsIgnoreCase("WHITE")){
            this.teamColor = TeamColor.WHITE;
        }else if (color.equalsIgnoreCase("BLACK")){
            this.teamColor = TeamColor.BLACK;
        }else{
            this.teamColor = null;
        }
        //userName = authDAO.getUser(authToken);
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

    }

    public void sendLeave() throws IOException {
        UserGameCommand leave = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        send(serializer.toJson(leave));
    }

    public void sendResign() throws IOException {
        UserGameCommand connect = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
        send(serializer.toJson(connect));
    }

    public void sendMakeMove(ChessMove move) throws IOException {
        MakeMoveCommand connect = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
        send(serializer.toJson(connect));
    }

    public void sendConnect() throws IOException{
        UserGameCommand connect = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        send(serializer.toJson(connect));
    }

    public void send(String msg) throws BadRequestException, IOException {
        this.session.getBasicRemote().sendText(msg);
    }

    @OnMessage
    public void onMessage(String message){
        var serializer = new Gson();
        var command = serializer.fromJson(message, ServerMessage.class);
        ServerMessage.ServerMessageType type = command.getServerMessageType();

        switch(type){
            case LOAD_GAME:
                LoadGameServerMessage msg = serializer.fromJson(message, LoadGameServerMessage.class);
                ChessGame game = msg.getGame();
                Board makeBoard = new Board();
                makeBoard.run(teamColor.toString(), game.getBoard());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
