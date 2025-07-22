package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() throws SQLException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()){
            for(var statement: createStatements){
                try(var preparedStatement = conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        }catch (SQLException x){
            throw new SQLException("Error creating tables");
        }
    }

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

    @Override
    public ArrayList<GameData> getList() throws DataAccessException{
        ArrayList<GameData> result = new ArrayList<>();
        String sql = "SELECT * FROM gameData";
        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){

            var rs = prepState.executeQuery();

            while(rs.next()){
                GameData newData = new GameData(rs.getInt("id"), rs.getString("whiteUsername"),
                        rs.getString("blackUsername"), rs.getString("gameName"));
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
        String sql = "SELECT gameID FROM gameData WHERE gameID = ? LIMIT 1";

        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){

            prepState.setInt(1, gameID);

            try(var rs = prepState.executeQuery()){
                return rs.next();
            }
        }catch (SQLException x){
            throw new DataAccessException("Error: game ID does not exist");
        }
    }

    @Override
    public Boolean checkPlayerColor(String playerColor, int gameID) throws DataAccessException{
        String sql = "SELECT whiteUsername, blackUsername FROM gameData";
        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){

            var rs = prepState.executeQuery();

            if(playerColor.equals("WHITE")){
                if(rs.getString("whiteUsername") == null){
                    return true;
                }else{
                    return false;
                }
            }else if(playerColor.equals("BLACK")){
                if(rs.getString("blackUsername") == null){
                    return true;
                }else{
                    return false;
                }
            }
            return false;
        }catch (SQLException x){
            throw new DataAccessException("Error: could not check player color");
        }
    }

    @Override
    public void setPlayerColor(String playerColor, int gameID, String userName) throws DataAccessException{
        String sql = "UPDATE gameData SET whiteUsername = '?' WHERE id = ?";
        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){

            prepState.setString(1, userName);
            prepState.setInt(2, gameID);

            prepState.executeUpdate();
        }catch (SQLException x){
            throw new DataAccessException("Error: could not set player color");
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
        String sql = "INSERT INTO gameData (gameID, gameName) VALUES ('?', '?')";
        try(var comm = DatabaseManager.getConnection();
            var prepState = comm.prepareStatement(sql)){

            prepState.setInt(1, gameID);
            prepState.setString(2, gameName);

            prepState.executeUpdate();

        }catch(SQLException x){
            throw new DataAccessException("Error: could not add new game");
        }
        return gameID;
    }
}
