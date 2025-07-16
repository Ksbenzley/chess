package dataaccess;
import model.GameData;
import java.util.ArrayList;

public interface GameDAO {
    public ArrayList<GameData> getList();
    public Boolean gameExists(String gameName);
    public Integer createGameID();
}
