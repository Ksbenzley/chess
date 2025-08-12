package websocket.commands;

import chess.ChessPosition;

public class MakeMoveCommand extends UserGameCommand{

    private ChessPosition start;
    private ChessPosition end;

    public MakeMoveCommand(CommandType commandType, String authToken, Integer gameID, ChessPosition start, ChessPosition end) {
        super(commandType, authToken, gameID);

        this.start = start;
        this.end = end;
    }

    public ChessPosition getStart(){
        return start;
    }

    public ChessPosition getEnd(){
        return end;
    }
}
