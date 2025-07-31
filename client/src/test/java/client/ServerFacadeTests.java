package client;

import dataaccess.*;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import exceptions.NotAuthorizedException;
import model.AuthData;
import server.*;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static ClientRequest client;
    AuthDAO auth;
    GameDAO game;
    UserDAO user;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
        client = new ClientRequest("http://localhost:" + port);
    }

    @BeforeEach
    public void before() throws SQLException, DataAccessException {
        game = new SQLGameDAO();
        user = new SQLUserDAO();
        auth = new SQLAuthDAO();
        game.clear();
        user.clear();
        auth.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    public void registerPass(){
        UserData user = new UserData("shinji", "ikari", "email");
        AuthData auth = facade.register(user);

        assertNotNull(auth.authToken());
        assertEquals("shinji", auth.username());
    }

    @Test
    public void registerFail(){
        UserData user = new UserData("shinji", "ikari", "email");
        UserData user2 = new UserData("shinji", "password2", "email");
        facade.register(user);

        assertThrows(AlreadyTakenException.class, () -> {
            facade.register(user2);
        });
    }

    @Test
    public void loginPass(){
        UserData user = new UserData("shinji", "ikari", "email");
        facade.register(user);
        facade.logout();
        AuthData authToken = facade.login(user);
        assertNotNull(authToken);
    }

    @Test
    public void loginFail(){
        UserData user = new UserData("shinji", "ikari", "email");
        assertThrows(NotAuthorizedException.class, () -> {
            facade.login(user);
        });
    }

    @Test
    public void logoutPass(){
        client.register("Shinji", "pass", "email");
        client.logout();

        assertThrows(NotAuthorizedException.class, () ->{
            client.loadGames();
        });
    }

    @Test
    public void logoutFail(){
        assertThrows(NotAuthorizedException.class, () ->{
            client.logout();
        });
    }

    @Test
    public void createPass(){
        UserData user = new UserData("Shinji", "pass", "email");
        facade.register(user);
        facade.createGame("newGame");
        assertNotNull(client.getGameList());
    }

    @Test
    public void createFail(){
        UserData user = new UserData("shinji", "pass", "email");
        facade.register(user);
        assertThrows(BadRequestException.class, () ->{
            client.createGame();
        });
    }

    @Test
    public void listPass(){
        UserData user = new UserData("shinji", "pass", "email");
        client.register(user.username(), user.password(), user.email());
        client.createGame("newGame");
        client.createGame("newGameTwo");
        client.listGames();
        assertFalse(client.getGameList().isEmpty());
    }

    @Test
    public void playPass(){
        UserData user = new UserData("shinji", "pass", "email");
        client.register(user.username(), user.password(), user.email());
        client.createGame("Nerv");
        client.listGames();
        assertEquals("", client.playGame("1", "WHITE"));
    }

    @Test
    public void playFail(){
        UserData user = new UserData("shinji", "pass", "email");
        client.register(user.username(), user.password(), user.email());
        client.createGame("Nerv");
        client.listGames();
        assertThrows(BadRequestException.class, () ->{
            client.playGame("gameName", "WHITE");
        });
    }

    @Test
    public void observePass(){
        UserData user = new UserData("shinji", "pass", "email");
        client.register(user.username(), user.password(), user.email());
        client.createGame("Nerv");
        client.listGames();
        assertEquals("", client.observeGame("1"));
    }

    @Test
    public void observeFail(){
        UserData user = new UserData("shinji", "pass", "email");
        client.register(user.username(), user.password(), user.email());
        client.createGame("Nerv");
        client.listGames();
        assertThrows(NumberFormatException.class, () ->{
            client.observeGame("gameName");
        });
    }
}
