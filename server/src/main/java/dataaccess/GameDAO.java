package dataaccess;
import model.GameData;
import java.util.ArrayList;

public interface GameDAO {
    public ArrayList<GameData> getList();
    public Boolean gameExists(String gameName);
    public Integer createGameID();
    public Boolean checkGameID(int gameID);
    public Boolean checkPlayerColor(String playerColor, int gameID);
    public void setPlayerColor(String playerColor, int gameID, String userName);
}
