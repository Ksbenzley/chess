package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.session.DatabaseAdaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthDAOTests {

    private static SQLAuthDAO auth;
    private static SQLUserDAO user;

    @BeforeEach
    public void update() throws SQLException, DataAccessException {
        auth = new SQLAuthDAO();
        user = new SQLUserDAO();
    }

    @Test
    public void clearPass() throws DataAccessException{
        user.clear();
        user.addUser("RadioHead", "password", "email@mail.com");
        auth.loginUser("RadioHead");
        auth.clear();
        String sql = "SELECT * FROM authData";
        try (var conn = DatabaseManager.getConnection();
             var prepState = conn.prepareStatement(sql)){

            try (var rs = prepState.executeQuery()){
                assertFalse(rs.next());
            }
        }catch(SQLException x){
            throw new DataAccessException("Error: could not clear");
        }
    }

    @Test
    public void logoutPass() throws DataAccessException{

    }

    @Test
    public void logoutFail() throws DataAccessException{

    }

    @Test
    public void getUserPass() throws DataAccessException{

    }

    @Test
    public void getUserFail() throws DataAccessException{

    }

    @Test
    public void loginUserPass() throws DataAccessException{
        user.clear();
        auth.clear();
        user.addUser("blackSabbath", "pass", "email");
        auth.loginUser("blackSabbath");
        String sql = "SELECT * FROM authData;";
        try (var conn = DatabaseManager.getConnection();
             var prepState = conn.prepareStatement(sql)){

            try (var rs = prepState.executeQuery()){
                assertTrue(rs.next());
            }
        }catch(SQLException x){
            throw new DataAccessException("Error: could not clear");
        }
    }

    @Test
    public void loginUserFail() throws DataAccessException{
        user.clear();
        auth.clear();
        auth.loginUser("blackSabbath");
        String sql = "SELECT * FROM authData;";
        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){

            try (var rs = prepState.executeQuery()){
                assertTrue(rs.next());
            }
        }catch(SQLException x){
            throw new DataAccessException("Error: could not clear");
        }
    }

    @Test
    public void checkUserAuthPass() throws DataAccessException{

    }

}
