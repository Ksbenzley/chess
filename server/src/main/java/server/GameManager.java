package server;

import chess.ChessGame;

import java.util.HashMap;

public class GameManager {

    private static final HashMap<Integer, ChessGame> Saver = new HashMap<>();

    public static void addGame(int gameID, ChessGame game){
        Saver.put(gameID, game);
    }

    public static void removeGame(int gameID){
        Saver.remove(gameID);
    }

    public static ChessGame getGame(int gameID){
        return Saver.get(gameID);
    }

}
