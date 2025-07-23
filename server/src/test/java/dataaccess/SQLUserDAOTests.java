package dataaccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTests {

    private static SQLUserDAO user;

    @BeforeEach
    public void update() throws SQLException, DataAccessException {
        user = new SQLUserDAO();
    }

    @Test
    public void clearTest() throws DataAccessException{
        user.addUser("username", "password", "email@gmail.com");
        user.clear();
        String sql = "SELECT * FROM userData";
        try(var conn = DatabaseManager.getConnection();
            var prepState = conn.prepareStatement(sql)){

            try (var rs = prepState.executeQuery()){
                assertFalse(rs.next());
            }
        }catch(SQLException x){
            throw new DataAccessException("Error: could not clear");
        }
    }

    @Test
    public void checkForUserTestPass() throws DataAccessException{
        user.clear();
        user.addUser("newUser", "password", "email@mail.com");
        user.checkForUser("newUser");
        assertTrue(user.checkForUser("newUser"));
    }

    @Test
    public void checkForUserTestFail() throws DataAccessException{
        user.clear();
        user.addUser("user2", "passcode", "email@mail.com");

        boolean result = user.checkForUser("wrongUser");
        assertFalse(result);
    }

    @Test
    public void addUserPass() throws DataAccessException{
        user.clear();
        user.addUser("Bingo", "BingoPass", "email@bongo.com");
        assertTrue(user.checkForUser("Bingo"));
    }

    @Test
    public void addUserFail() throws DataAccessException{
        user.clear();
        assertThrows(DataAccessException.class, () ->{
            user.addUser(null, null, null);
        });
    }

    @Test
    public void checkPassPass() throws DataAccessException{
        user.clear();
        user.addUser("Bingo", "Bongo", "email@mail.com");
        assertTrue(user.checkPass("Bingo", "Bongo"));
    }

    @Test
    public void checkPassFail() throws DataAccessException{
        user.clear();
        user.addUser("Bingo", "Bongo", "email@mail.com");
        assertFalse(user.checkPass("Bingo", "wrongPass"));
    }
}
