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
        game = new MemoryGameDAO();
    }

    @Test
    public void joinGamePass() throws BadRequestException, NotAuthorizedException, AlreadyTakenException{
        String user = "player1";
        String authToken = auth.loginUser(user);
        int gameID = game.createGameID();
        game.addGame(gameID, "gamename");
        game.setPlayerColor("BLACK", gameID, null);
        GameService.joinGame(authToken, "BLACK", gameID, auth, game);
    }

    @Test
    public void joinGameFail() throws BadRequestException, NotAuthorizedException, AlreadyTakenException{
        String user = "player";
        String authToken = auth.loginUser(user);
        int gameID = game.createGameID();
        game.addGame(gameID, "gamename");
        game.setPlayerColor("BLACK", gameID, null);
        assertThrows(BadRequestException.class, () ->{
            GameService.joinGame(authToken, null, gameID, auth, game);
        });
    }

    @Test
    public void createGamePass() throws BadRequestException, NotAuthorizedException{
        String user = "player";
        String authToken = auth.loginUser(user);
        GameService.createGame(authToken, "gameName", auth, game);
    }

    @Test
    public void createGameFail() throws BadRequestException, NotAuthorizedException{
        String user = "player";
        String authToken = auth.loginUser(user);
        assertThrows(BadRequestException.class, () ->{
            GameService.createGame(authToken, null, auth, game);
        });
    }

    @Test
    public void listGamesPass() throws NotAuthorizedException{
        String user = "user";
        String authToken = auth.loginUser(user);
        GameService.listGames(authToken, auth, game);
    }

    @Test
    public void listGamesFail() throws NotAuthorizedException{
        String authToken = "1b3a-3d9t-0o-5k1e-8n91";
        assertThrows(NotAuthorizedException.class, () ->{
            GameService.listGames(authToken, auth, game);
        });
    }
}
