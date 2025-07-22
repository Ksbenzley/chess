package dataaccess;

import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class SQLUserDAO implements UserDAO{
    public SQLUserDAO() throws SQLException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()){
            for(var statement: createStatement){
                try(var preparedStatement = conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        }catch (SQLException x){
            throw new SQLException("Error creating tables");
        }
    }

    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS userData (
                username varchar(256) PRIMARY KEY,
                password varchar(256),
                email varchar(256)
            )
            """
    };
    @Override
    public void clear() throws DataAccessException{
        String sql = "TRUNCATE TABLE userData";

        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){

            prepState.executeUpdate();

        }catch(SQLException x){
            throw new DataAccessException("Error: could not clear");
        }
    }

    @Override
    public Boolean checkForUser(String username) throws DataAccessException{
        String sql = "SELECT 1 FROM userData WHERE username = ? LIMIT 1;";
        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){

            prepState.setString(1, username);

            try(var rs = prepState.executeQuery()){
                return rs.next();
            }
        }catch (SQLException x){
            throw new DataAccessException("Error: could not retrieve information");
        }
    }

    @Override
    public void addUser(String username, String password, String email) throws DataAccessException{
        String sql = "INSERT INTO userData(username, password, email) VALUES (?, ?, ?);";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){
            prepState.setString(1, username);
            prepState.setString(2, hashedPassword);
            prepState.setString(3, email);

            prepState.executeUpdate();

        }catch (SQLException x){
            throw new DataAccessException("Error: could not add information");
        }
    }

    @Override
    public Boolean checkPass(String username, String password) throws DataAccessException{
        String sql = "SELECT password FROM userData WHERE username = ? AND password = ?;";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){
            prepState.setString(1, username);
            prepState.setString(2, hashedPassword);

            try(var rs = prepState.executeQuery()){
                return rs.next();
            }
        }catch(SQLException x){
            throw new DataAccessException("Error: passwords don't match");
        }
    }
}
