package websocket.messages;

import com.google.gson.Gson;

public class ErrorServerMessage extends ServerMessage{

    String errorMessage;

    public ErrorServerMessage(String errorMessage){
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    public String toString(){
        return new Gson().toJson(this);
    }

    public String getErrorMessage(){
        return errorMessage;
    }

}
