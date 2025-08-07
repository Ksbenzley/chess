package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.LoadGameServerMessage;
import websocket.messages.NotificationServerMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String userName, Session session){
        Connection con = new Connection(userName, session);

        if(!connections.containsKey(gameID)){
            connections.put(gameID, new ArrayList<>());
        }

        connections.get(gameID).add(con);
    }

    public void makeMove(int gameID){

    }

    public void remove(int gameID, Session session){
        ArrayList<Connection> connectionList = connections.get(gameID);
        for (var connection : connectionList){
            if(connection.session.equals(session)){
                connectionList.remove(session);
            }
        }
    }

    public void resignGame(int gameID, Session session){
        ArrayList<Connection> connectionList = connections.get(gameID);
        connectionList.remove(session);
    }

    public void broadcastToOnly(int gameID, Session session, LoadGameServerMessage message) throws IOException {
        ArrayList<Connection> connectionList = connections.get(gameID);
        if(connectionList == null){ return; };
        for (var connection : connectionList){
            if(connection.session.equals(session)){
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

    public void broadcastToAll(int gameID, NotificationServerMessage message) throws IOException {
        ArrayList<Connection> connectionList = connections.get(gameID);
        if(connectionList == null){ return; };
        for (var connection : connectionList){
            connection.session.getRemote().sendString(message.toString());
        }
    }

}
