package service;
import static org.junit.jupiter.api.Assertions.*;

import dataaccess.*;
import org.junit.jupiter.api.*;


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
    public void registerTest() throws DataAccessException, BadRequestException, AlreadyTakenException{
        userService.registerUser("username", "password", "e@mail.com", user, auth);
    }
}
