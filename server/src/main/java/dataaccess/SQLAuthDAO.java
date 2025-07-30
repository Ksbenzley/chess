package dataaccess;

import exceptions.DataAccessException;
import exceptions.NotAuthorizedException;

import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authData (
                username varchar(256),
                authToken varchar(256)
            )
            """
    };

    public SQLAuthDAO() throws SQLException, DataAccessException {
        DatabaseManager.constructor(createStatements);
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "TRUNCATE TABLE authData;";

        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){

            prepState.executeUpdate();

        }catch(SQLException x){
            throw new DataAccessException("Error: could not clear");
        }
    }

    @Override
    public void logout(String authToken) throws DataAccessException, NotAuthorizedException {
        String sql = "DELETE FROM authData WHERE authToken = ?;";
        try(var comm = DatabaseManager.getConnection();
            var prepState = comm.prepareStatement(sql)){
            prepState.setString(1, authToken);

            int rowsAffected = prepState.executeUpdate();
            if(rowsAffected == 0){
                throw new NotAuthorizedException("Error: unauthorized");
            }
        }catch(SQLException x){
            throw new DataAccessException("Error: could not logout");
        }
    }

    @Override
    public String getUser(String authToken) throws DataAccessException{
        String username = "";
        String sql = "SELECT username FROM authData WHERE authToken = ? LIMIT 1;";
        try(var comm = DatabaseManager.getConnection();
            var prepState = comm.prepareStatement(sql)){

            prepState.setString(1, authToken);

            try(var rs = prepState.executeQuery()){
                if(rs.next()){
                    username = rs.getString("username");
                }
            }
        }catch(SQLException x){
            throw new DataAccessException("Error: could not retrieve user", x);
        }
        return username;
    }

    @Override
    public String createAuthToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String loginUser(String username) throws DataAccessException{
        String token = createAuthToken();
        String sql = "INSERT INTO authData (username, authToken) VALUES (?, ?);";
        try(var comm = DatabaseManager.getConnection();
            var prepState = comm.prepareStatement(sql)){
            prepState.setString(1, username);
            prepState.setString(2, token);

            prepState.executeUpdate();
        }catch(SQLException x){
            throw new DataAccessException("Error: cannot login user " + x.getMessage());
        }
        return token;
    }

    @Override
    public Boolean checkUserAuth(String authToken) throws DataAccessException{
        String sql = "SELECT username FROM authData WHERE authToken = ? LIMIT 1";
        try(var comm = DatabaseManager.getConnection();
            var prepState = comm.prepareStatement(sql)){
            prepState.setString(1, authToken);

            try(var rs = prepState.executeQuery()){
                return rs.next();
            }
        }catch(SQLException x){
            throw new DataAccessException("Error: user unauthorized");
        }
    }
}
