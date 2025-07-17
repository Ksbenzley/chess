package service;

import dataaccess.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private static MemoryUserDAO user;
    private static MemoryAuthDAO auth;
    private static MemoryGameDAO game;

    @BeforeEach
    public void update(){
        user = new MemoryUserDAO();
        auth = new MemoryAuthDAO();
    }

    @Test
    public void joinGamePass() throws BadRequestException, NotAuthorizedException, AlreadyTakenException{
        GameService.joinGame()
    }
}
