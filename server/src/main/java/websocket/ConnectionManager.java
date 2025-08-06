package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;

import javax.management.Notification;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String userName, Session session){
        Connection con = new Connection(userName, session);

        if(!connections.contains(gameID)){
            connections.put(gameID, new ArrayList<>());
        }

        connections.get(gameID).add(con);
    }

    public void broadcast(){

    }

}
