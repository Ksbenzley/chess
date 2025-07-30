package service;

import dataaccess.*;
import exceptions.DataAccessException;
import exceptions.NotAuthorizedException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseServiceTest {
    private static MemoryUserDAO user;
    private static MemoryAuthDAO auth;
    private static MemoryGameDAO game;

    @BeforeEach
    public void update(){
        user = new MemoryUserDAO();
        auth = new MemoryAuthDAO();
        game = new MemoryGameDAO();
    }

    @Test
    public void clearPass() throws DataAccessException {
        user.addUser("username", "password", "email");
        auth.loginUser("username");
        game.addGame(1982, "gameName");
        DatabaseService.clear(user, auth, game);
    }

    @Test
    public void logoutPass() throws NotAuthorizedException, DataAccessException{
        String user = "player";
        String authToken = auth.loginUser(user);
        DatabaseService.logout(authToken, auth);
    }

    @Test
    public void logoutFail() throws NotAuthorizedException{
        String authToken = "1b3a-3d9t-0o-5k1e-8n91";
        assertThrows(NotAuthorizedException.class, () ->{
            DatabaseService.logout(authToken, auth);
        });
    }

    @Test
    public void checkAuthTokenPass() throws DataAccessException{
        String user = "player";
        String authToken = auth.loginUser(user);
        assertTrue(DatabaseService.checkAuthToken(authToken, auth));
    }

    @Test
    public void checkAuthTokenFail() throws DataAccessException{
        String authToken = "1b3a-3d9t-0o-5k1e-8n91";
        assertFalse(DatabaseService.checkAuthToken(authToken, auth));
    }
}
