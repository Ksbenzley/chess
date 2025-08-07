package websocket.messages;

import chess.ChessGame;
import com.google.gson.Gson;

public class LoadGameServerMessage extends ServerMessage {

    ChessGame game;
    public LoadGameServerMessage(ChessGame game){
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }

    public String toString(){
        return new Gson().toJson(this);
    }


}
