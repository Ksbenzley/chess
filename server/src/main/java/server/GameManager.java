package server;

import chess.ChessGame;

import java.util.HashMap;

public class GameManager {

    private static final HashMap<Integer, ChessGame> saverMap = new HashMap<>();

    public static void addGame(int gameID, ChessGame game){
        saverMap.put(gameID, game);
    }

    public static void removeGame(int gameID){
        saverMap.remove(gameID);
    }

    public static ChessGame getGame(int gameID){
        return saverMap.get(gameID);
    }

}
