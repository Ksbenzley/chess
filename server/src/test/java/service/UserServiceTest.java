package service;

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
        UserService.registerUser("username", "password", "e@mail.com", user, auth);
    }
}
