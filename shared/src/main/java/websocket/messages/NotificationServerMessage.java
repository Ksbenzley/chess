package websocket.messages;

import com.google.gson.Gson;

public class NotificationServerMessage extends ServerMessage{

    String message;

    public NotificationServerMessage(String message){
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String toString(){
        return new Gson().toJson(this);
    }
}
