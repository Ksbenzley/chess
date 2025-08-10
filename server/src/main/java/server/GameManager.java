package server;

import chess.ChessGame;

import java.util.HashMap;

public class GameManager {

    private static final HashMap<Integer, ChessGame> SAVER = new HashMap<>();

    public static void addGame(int gameID, ChessGame game){
        SAVER.put(gameID, game);
    }

    public static void removeGame(int gameID){
        SAVER.remove(gameID);
    }

    public static ChessGame getGame(int gameID){
        return SAVER.get(gameID);
    }

}
