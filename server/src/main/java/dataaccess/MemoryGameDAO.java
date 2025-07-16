package dataaccess;
import model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MemoryGameDAO implements GameDAO{
    static HashMap<Integer, GameData> gameDB = new HashMap<>();

    public Integer createGameID(){
        int gameID = 1000 + new Random().nextInt(9000);
        return gameID;
    }

    public ArrayList<GameData> getList(){
        ArrayList<GameData> games = new ArrayList<>();
        gameDB.forEach((k, v) -> {games.add(v); } );
        return games;
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
