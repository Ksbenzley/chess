package websocket.messages;

import com.google.gson.Gson;

public class NotificationServerMessage extends ServerMessage{

    String notificationText;

    public NotificationServerMessage(String notificationText){
        super(ServerMessageType.NOTIFICATION);
        this.notificationText = notificationText;
    }

    public String toString(){
        return new Gson().toJson(this);
    }
}
