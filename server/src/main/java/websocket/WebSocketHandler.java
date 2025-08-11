package websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exceptions.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorServerMessage;
import websocket.messages.LoadGameServerMessage;
import server.*;
import websocket.messages.NotificationServerMessage;

import javax.management.Notification;
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
        String userName;
        try {
            userName = auth.getUser(command.getAuthToken());
        }catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
        ArrayList<GameData> gameData = game.getList();
        Boolean isObserver = null;
        GameData currentGame = null;
        for (GameData game : gameData) {
            if (game.gameID() == gameID) {
                currentGame = game;
            }
        }

        if(currentGame == null){
            ErrorServerMessage msg = new ErrorServerMessage("Error: bad game ID");
            manager.broadcastToOnly(gameID, session, msg);
            return;
        }

        if(!auth.checkUserAuth(command.getAuthToken())){
            ErrorServerMessage msg = new ErrorServerMessage("Error: bad authToken");
            manager.broadcastToOnly(gameID, session, msg);
            return;
        }

        if((currentGame.blackUsername() != null && currentGame.blackUsername().equalsIgnoreCase(userName)) ||
                (currentGame.whiteUsername() != null && currentGame.whiteUsername().equalsIgnoreCase(userName))){
            isObserver = false;
        }else{
            isObserver = true;
        }

        String color = "";

        if(currentGame.whiteUsername() != null && currentGame.whiteUsername().equals(userName)){
            color = "WHITE";
        }else if(currentGame.blackUsername() != null && currentGame.blackUsername().equals(userName)){
            color = "BLACK";
        }

        switch(command.getCommandType()){
            case CONNECT:
                connectCommand(gameID, session, userName, isObserver, color);
                break;

            case LEAVE:
                leaveCommand(gameID, session, userName, color, isObserver, currentGame);
                break;

            case RESIGN:
                resignCommand(gameID, session, isObserver, userName);
                return;

            case MAKE_MOVE:
                makeMoveCommand(gameID, session, isObserver, color, userName, message);
                break;
        }
    }

    public void makeMoveCommand(int gameID, Session session, Boolean isObserver, String color, String userName, String message) throws IOException {
        Gson makeSerializer = new Gson();
        MakeMoveCommand makeCommand = makeSerializer.fromJson(message, MakeMoveCommand.class);
        ChessMove move = makeCommand.getMove();

        ChessGame chessGameData = GameManager.getGame(gameID);
        ChessBoard realBoard = chessGameData.getBoard();

        //checking if move is made by an observer
        if(isObserver){
            ErrorServerMessage observerMove = new ErrorServerMessage("Error: observer cannot make moves");
            manager.broadcastToOnly(gameID, session, observerMove);
            return;
        }

        //checking if game is over
        if(chessGameData.isInCheckmate(ChessGame.TeamColor.WHITE) || chessGameData.isInCheckmate(ChessGame.TeamColor.BLACK)){
            chessGameData.setGameOver(true);
        }
        if(chessGameData.getGameOver()){
            ErrorServerMessage gameOver = new ErrorServerMessage("Error: game is over");
            manager.broadcastToOnly(gameID, session, gameOver);
            return;
        }

        //checking for valid move
        if(manager.validateMove(move, realBoard)){

            //checking for wrong turn
            if(!chessGameData.getTeamTurn().equals(ChessGame.TeamColor.valueOf(color))){
                ErrorServerMessage wrongTurn = new ErrorServerMessage("Error: not your turn");
                manager.broadcastToOnly(gameID, session, wrongTurn);
                return;
            }
            manager.makeMove(move, realBoard);

            //switch team turn
            if(chessGameData.getTeamTurn() == ChessGame.TeamColor.WHITE){
                chessGameData.setTeamTurn(ChessGame.TeamColor.BLACK);
            }else{
                chessGameData.setTeamTurn(ChessGame.TeamColor.WHITE);
            }

            //sending notifications
            LoadGameServerMessage updateGame = new LoadGameServerMessage(GameManager.getGame(gameID));
            manager.broadcastToAll(gameID, updateGame);
            NotificationServerMessage notification = new NotificationServerMessage(
                    userName + " moved from " + translate(move.getStartPosition()) + " to " + translate(move.getEndPosition()));
            manager.broadcastToAllExcept(gameID, session, notification);
            if(chessGameData.isInCheckmate(chessGameData.getTeamTurn())){
                NotificationServerMessage checkMate = new NotificationServerMessage(chessGameData.getTeamTurn() + " is now in check mate");
                manager.broadcastToAll(gameID, checkMate);
            }else if(chessGameData.isInCheck(chessGameData.getTeamTurn())){
                NotificationServerMessage checkMate = new NotificationServerMessage(chessGameData.getTeamTurn() + " is now in check");
                manager.broadcastToAll(gameID, checkMate);
            }
            //switch team turn here?
        }else{
            ErrorServerMessage invalidMove = new ErrorServerMessage("Error: invalid move");
            manager.broadcastToOnly(gameID, session, invalidMove);
        }
    }

    public void resignCommand(int gameID, Session session, Boolean isObserver, String userName) throws IOException {
        ChessGame chessGame = GameManager.getGame(gameID);
        if(isObserver){
            ErrorServerMessage observerResign = new ErrorServerMessage("Error: observer cannot resign");
            manager.broadcastToOnly(gameID, session, observerResign);
            return;
        }
        if(chessGame.getGameOver()){
            ErrorServerMessage alreadyResigned = new ErrorServerMessage("Error: the game has already finished");
            manager.broadcastToOnly(gameID, session, alreadyResigned);
            return;
        }
        manager.resignGame(gameID, session);
        NotificationServerMessage resigned = new NotificationServerMessage(userName + " has resigned");
        manager.broadcastToAll(gameID, resigned);
        chessGame.setGameOver(true);
        GameManager.removeGame(gameID);
        GameManager.addGame(gameID, chessGame);
    }

    public void leaveCommand(int gameID, Session session, String userName, String color, Boolean isObserver, GameData currentGame)
            throws IOException, DataAccessException {
        if(isObserver){
            NotificationServerMessage leftGame = new NotificationServerMessage(userName + " has left the game");
            manager.broadcastToBlack(gameID, leftGame, currentGame.blackUsername());
            game.setPlayerColor(color, gameID, null);
            manager.remove(gameID, session);
            return;
        }
        NotificationServerMessage leftGame = new NotificationServerMessage(userName + " has left the game");
        manager.broadcastToAllExcept(gameID, session, leftGame);
        game.setPlayerColor(color, gameID, null);
        manager.remove(gameID, session);
    }

    public void connectCommand(int gameID, Session session, String userName, Boolean isObserver, String color) throws IOException {
        //game.setPlayerColor(color, gameID, userName);
        manager.add(gameID, userName, session);
        LoadGameServerMessage msg = new LoadGameServerMessage(GameManager.getGame(gameID));
        manager.broadcastToOnly(gameID, session, msg);
        if(isObserver){
            NotificationServerMessage notif = new NotificationServerMessage(userName + " joined the game as an observer");
            manager.broadcastToAllExcept(gameID, session, notif);
        }else {
            NotificationServerMessage notif = new NotificationServerMessage(userName + " joined the game as " + color);
            manager.broadcastToAllExcept(gameID, session, notif);
        }
    }

    public String translate(ChessPosition pos){
        String translatedPos = "";

        switch(pos.getColumn()){
            case 1 -> translatedPos = "a" + pos.getRow();
            case 2 -> translatedPos = "b" + pos.getRow();
            case 3 -> translatedPos = "c" + pos.getRow();
            case 4 -> translatedPos = "d" + pos.getRow();
            case 5 -> translatedPos = "e" + pos.getRow();
            case 6 -> translatedPos = "f" + pos.getRow();
            case 7 -> translatedPos = "g" + pos.getRow();
            case 8 -> translatedPos = "h" + pos.getRow();
        }
        return translatedPos;
    }

}
