package service;
import dataaccess.*;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import exceptions.NotAuthorizedException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private static MemoryUserDAO user;
    private static MemoryAuthDAO auth;

    @BeforeEach
    public void update(){
        user = new MemoryUserDAO();
        auth = new MemoryAuthDAO();
    }

    @Test
    public void registerUserPass() throws BadRequestException, AlreadyTakenException, DataAccessException {
        UserService.registerUser("player", "password", "e@mail.com", user, auth);
        assertTrue(user.checkForUser("player"));
    }

    @Test
    public void registerUserFail() throws BadRequestException, AlreadyTakenException{
        assertThrows(AlreadyTakenException.class, () ->{
            UserService.registerUser("username", "password", "e@mail.com", user, auth);
            UserService.registerUser("username", "password", "e@mail.com", user, auth);
        });
    }

    @Test
    public void loginUserPass() throws NotAuthorizedException, BadRequestException, DataAccessException{
        user.addUser("username", "password", "email@mail.com");
        String authToken = UserService.loginUser("username", "password", user, auth);
        assertNotNull(authToken);
    }

    @Test
    public void loginUserFail() throws NotAuthorizedException, BadRequestException{
        assertThrows(BadRequestException.class, ()->{
            UserService.loginUser(null, "password", user, auth);
        });
    }

    @Test
    public void checkUserPass() throws DataAccessException {
        user.addUser("existingUser", "password", "email@mail.com");
        boolean result = UserService.checkUser("existingUser", user);
        assertTrue(result);
    }

    @Test
    public void checkUserFail() throws DataAccessException{
        boolean result = UserService.checkUser("nonexistentUser", user);
        assertFalse(result);
    }







}


