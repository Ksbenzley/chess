package dataaccess;
import chess.ChessBoard;
import exceptions.DataAccessException;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    public ArrayList<GameData> getList() throws DataAccessException;
    public Boolean gameExists(String gameName) throws DataAccessException;
    public Integer createGameID();
    public Boolean checkGameID(int gameID) throws DataAccessException;
    public Boolean checkPlayerColor(String playerColor, int gameID) throws DataAccessException;
    public void setPlayerColor(String playerColor, int gameID, String userName) throws DataAccessException;
    public void clear() throws DataAccessException;
    public Integer addGame(int gameID, String gameName) throws DataAccessException;
}
