package service;
import dataaccess.*;
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
    //create a new user
    public void registerUserPass() throws BadRequestException, AlreadyTakenException{
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
    public void loginUserPass() throws NotAuthorizedException, BadRequestException{
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
    public void checkUserPass() {
        user.addUser("existingUser", "password", "email@mail.com");
        boolean result = UserService.checkUser("existingUser", user);
        assertTrue(result);
    }

    @Test
    public void checkUserFail() {
        boolean result = UserService.checkUser("nonexistentUser", user);
        assertFalse(result);
    }







}


