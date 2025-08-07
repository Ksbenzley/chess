package server;

import chess.ChessGame;

import java.util.HashMap;

public class GameManager {

    private static final HashMap<Integer, ChessGame> saver = new HashMap<>();

    public static void addGame(int gameID, ChessGame game){
        saver.put(gameID, game);
    }

    public static ChessGame getGame(int gameID){
        return saver.get(gameID);
    }

}
