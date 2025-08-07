package websocket.messages;

import com.google.gson.Gson;

public class ErrorServerMessage extends ServerMessage{

    String notificationText;

    public ErrorServerMessage(String notificationText){
        super(ServerMessageType.ERROR);
        this.notificationText = notificationText;
    }

    public String toString(){
        return new Gson().toJson(this);
    }

}
