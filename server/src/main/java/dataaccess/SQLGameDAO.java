package dataaccess;

import chess.ChessBoard;
import com.google.gson.Gson;
import exceptions.DataAccessException;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class SQLGameDAO implements GameDAO{

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS gameData (
                id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
                whiteUsername varchar(256),
                blackUsername varchar(256),
                gameName varchar(256)
            )
            """
    };

    public SQLGameDAO() throws SQLException, DataAccessException {
        DatabaseManager.constructor(createStatements);
    }

    @Override
    public ArrayList<GameData> getList() throws DataAccessException{
        ArrayList<GameData> result = new ArrayList<>();
        String sql = "SELECT * FROM gameData";
        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){

            var rs = prepState.executeQuery();

            while(rs.next()){
                GameData newData = new GameData(rs.getInt("id"), rs.getString("whiteUsername"),
                        rs.getString("blackUsername"), rs.getString("gameName"), null);
                result.add(newData);
            }
        }catch (SQLException x){
            throw new DataAccessException("Error: could not retrieve list");
        }
        return result;
    }

    @Override
    public Boolean gameExists(String gameName) throws DataAccessException{
        String sql = "SELECT 1 FROM gameData WHERE gameName = ? LIMIT 1";
        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){

            prepState.setString(1, gameName);

            try(var rs = prepState.executeQuery()){
                return rs.next();
            }
        }catch(SQLException e){
            throw new DataAccessException("Error: data access error");
        }
    }

    @Override
    public Integer createGameID() {
        return 1000 + new Random().nextInt(9000);
    }

    @Override
    public Boolean checkGameID(int gameID) throws DataAccessException {
        String sql = "SELECT id FROM gameData WHERE id = ? LIMIT 1";

        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){

            prepState.setInt(1, gameID);

            try(var rs = prepState.executeQuery()){
                return rs.next();
            }
        }catch (SQLException x){
            throw new DataAccessException("Error: game ID does not exist", x);
        }
    }

    @Override
    public Boolean checkPlayerColor(String playerColor, int gameID) throws DataAccessException{
        String sql = "SELECT whiteUsername, blackUsername FROM gameData WHERE id = ?;";
        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){

            prepState.setInt(1, gameID);

            var rs = prepState.executeQuery();

            if(playerColor.equals("WHITE")){
                if(rs.next() && rs.getString("whiteUsername") == null){
                    return true;
                }else{
                    return false;
                }
            }else if(playerColor.equals("BLACK")){
                if(rs.next() && rs.getString("blackUsername") == null){
                    return true;
                }else{
                    return false;
                }
            }
            return false;
        }catch (SQLException x){
            throw new DataAccessException("Error: could not check player color", x);
        }
    }

    @Override
    public void setPlayerColor(String playerColor, int gameID, String userName) throws DataAccessException{
        String column;
        if(playerColor.equals("WHITE")){
            column = "whiteUsername";
        }else if (playerColor.equals("BLACK")){
            column = "blackUsername";
        }else{
            throw new DataAccessException("Error: invalid color");
        }
        String sql = "UPDATE gameData SET " + column + " = ? WHERE id = ?;";
        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){

            prepState.setString(1, userName);
            prepState.setInt(2, gameID);

            prepState.executeUpdate();
        }catch (SQLException x){
            throw new DataAccessException("Error: could not set player color", x);
        }
    }

    @Override
    public void clear() throws DataAccessException{
        String sql = "TRUNCATE TABLE gameData";

        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){

            prepState.executeUpdate();

        }catch(SQLException x){
            throw new DataAccessException("Error: could not clear");
        }
    }

    @Override
    public Integer addGame(int gameID, String gameName) throws DataAccessException{
        String sql = "INSERT INTO gameData (id, gameName) VALUES (?, ?);";
        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){

            prepState.setInt(1, gameID);
            prepState.setString(2, gameName);

            prepState.executeUpdate();

        }catch(SQLException x){
            throw new DataAccessException("Error: could not add new game");
        }
        return gameID;
    }
}
