package websocket;

import chess.ChessMove;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.LoadGameServerMessage;
import websocket.messages.NotificationServerMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import chess.*;
import websocket.messages.ServerMessage;

public class ConnectionManager {

    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String userName, Session session){
        Connection con = new Connection(userName, session);

        if(!connections.containsKey(gameID)){
            connections.put(gameID, new ArrayList<>());
        }
        connections.get(gameID).add(con);
    }

    public void makeMove(ChessMove move, ChessBoard board){
        board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        board.removePiece(move.getStartPosition());
    }

    public Boolean validateMove(ChessMove move, ChessBoard board){
        ChessPiece piece = board.getPiece(move.getStartPosition());
        for (ChessMove availableMove : piece.pieceMoves(board, move.getStartPosition())){
            if(availableMove.getEndPosition().equals(move.getEndPosition())){
                return true;
            }
        }
        return false;
    }

    public void remove(int gameID, Session session){
        ArrayList<Connection> connectionList = connections.get(gameID);
        for (var connection : connectionList){
            if(connection.session.equals(session)){
                connectionList.remove(connection);
            }
        }
    }

    public void resignGame(int gameID, Session session){
        ArrayList<Connection> connectionList = connections.get(gameID);
        connectionList.remove(session);
    }

    public void broadcastToOnly(int gameID, Session session, ServerMessage message) throws IOException {
        session.getRemote().sendString(message.toString());
    }

    public void broadcastToBlack(int gameID, ServerMessage message, String blackUserName) throws IOException {
        ArrayList<Connection> connectionList = connections.get(gameID);
        if(connectionList == null){ return; };
        for (var connection : connectionList){
            if(connection.userName.equalsIgnoreCase(blackUserName)){
                connection.session.getRemote().sendString(message.toString());
            }
        }
    }

    public void broadcastToAllExcept(int gameID, Session session, NotificationServerMessage message) throws IOException {
        ArrayList<Connection> connectionList = connections.get(gameID);
        if(connectionList == null){ return; };
        for (var connection : connectionList){
            if(!connection.session.equals(session)){
                connection.session.getRemote().sendString(message.toString());
            }
        }
    }

    public void broadcastToAll(int gameID, ServerMessage message) throws IOException {
        ArrayList<Connection> connectionList = connections.get(gameID);
        if(connectionList == null){ return; };
        for (var connection : connectionList){
            connection.session.getRemote().sendString(message.toString());
        }
    }

}
