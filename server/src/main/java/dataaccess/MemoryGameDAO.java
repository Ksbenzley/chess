package dataaccess;
import model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MemoryGameDAO implements GameDAO{
    static HashMap<Integer, GameData> gameDB = new HashMap<>();

    public void clear(){
        gameDB.clear();
    }

    public void setPlayerColor(String playerColor, int gameID, String userName){
        GameData currentGame = gameDB.get(gameID);
        if(playerColor.equals("WHITE")){
            GameData newGame = new GameData(gameID, userName, currentGame.blackUsername(), currentGame.gameName());
            gameDB.replace(gameID, newGame);
        }else if(playerColor.equals("BLACK")){
            GameData newGame = new GameData(gameID, currentGame.whiteUsername(), userName, currentGame.gameName());
            gameDB.replace(gameID, newGame);
        }
    }

    public Boolean checkPlayerColor(String playerColor, int gameID){
        if(playerColor.equals("WHITE")){
            if(gameDB.get(gameID).whiteUsername() == null){
                return true;
            }else{
                return false;
            }
        }else if(playerColor.equals("BLACK")){
            if(gameDB.get(gameID).blackUsername() == null){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public Integer createGameID(){
        int gameID = 1000 + new Random().nextInt(9000);
        return gameID;
    }

    public ArrayList<GameData> getList(){
        ArrayList<GameData> games = new ArrayList<>();
        gameDB.forEach((k, v) -> {games.add(v); } );
        return games;
    }

    public Boolean checkGameID(int gameID){
        if(gameDB.containsKey(gameID)){
            return true;
        }
        return false;
    }

    public Boolean gameExists(String gameName) {
        ArrayList<GameData> games = new ArrayList<>();
        gameDB.forEach((k, v) -> {
            games.add(v);
        });
        for(GameData game : games){
            if(game.gameName().equals(gameName)){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public Integer addGame(int gameID, String gameName){
        GameData game = new GameData(gameID, null, null, gameName);
        gameDB.put(gameID, game);
        return gameID;
    }

}
