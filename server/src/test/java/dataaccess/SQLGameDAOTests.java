package dataaccess;

import exceptions.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTests {

    private static SQLGameDAO game;

    @BeforeEach
    public void update() throws SQLException, DataAccessException {
        game = new SQLGameDAO();
    }

    @Test
    public void getListPass() throws DataAccessException{
        game.addGame(1969, "moonGame");
        game.addGame(2015, "MGSV");
        game.getList();
        String sql = "SELECT * FROM gameData";
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
    public void getListFail() throws DataAccessException{
        String sql = "DROP TABLE IF EXISTS gameData;";
        try (var conn = DatabaseManager.getConnection();
             var prepState = conn.prepareStatement(sql)) {

            prepState.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error: could not find table");
        }
        assertThrows(DataAccessException.class, () -> game.getList());
    }

    @Test
    public void gameExistsPass() throws DataAccessException{
        game.clear();
        game.addGame(2049, "BladeRunner");
        assertTrue(game.gameExists("BladeRunner"));
    }

    @Test
    public void gameExistsFail() throws DataAccessException{
        game.clear();
        game.addGame(2049, "BladeRunner");
        assertFalse(game.gameExists("Deckard"));
    }

    @Test
    public void createGameIDPass(){
        int gameID = game.createGameID();
        assertTrue(gameID >= 1000 && gameID <= 9999);
    }

    @Test
    public void checkGameIDPass() throws DataAccessException{
        game.clear();
        game.addGame(1999, "centuryTurn");
        assertTrue(game.checkGameID(1999));
    }

    @Test
    public void checkGameIDFail() throws DataAccessException{
        game.clear();
        game.addGame(1945, "endOfWar");
        assertFalse(game.checkGameID(2903));
    }

    @Test
    public void checkPlayerColorPass() throws DataAccessException{
        game.clear();
        game.addGame(2938, "gameName");
        game.setPlayerColor("WHITE", 2938, "userName");
        assertNotNull(game.checkPlayerColor("WHITE", 2938));
    }

    @Test
    public void checkPlayerColorFail() throws DataAccessException{
        game.clear();
        game.addGame(3328, "game3");
        game.setPlayerColor("BLACK", 3328, "Jester");
        assertFalse(game.checkPlayerColor("BLACK", 3328));
    }

    @Test
    public void setPlayerColorPass() throws DataAccessException{
        game.addGame(2001, "Space Odyssey");
        game.setPlayerColor("WHITE", 2001, "David Bowman");
        String sql = "SELECT whiteUsername FROM gameData WHERE id = ?";
        try (var conn = DatabaseManager.getConnection();
             var prepState = conn.prepareStatement(sql)) {
            prepState.setInt(1, 2001);
            try(var rs = prepState.executeQuery()){
                assertTrue(rs.next());
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: could not find table");
        }
    }

    @Test
    public void setPlayerColorFail() throws DataAccessException{
        game.clear();
        game.addGame(3000, "oogaBooga");
        assertThrows(DataAccessException.class, () ->{
            game.setPlayerColor("GREEN", 3000, "heehee");
        });
    }

    @Test
    public void clearPass() throws DataAccessException {
        game.addGame(3928, "game");
        game.clear();
        String sql = "SELECT * FROM gameData;";
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
    public void addGamePass() throws DataAccessException{
        game.addGame(3000, "gameName");
        assertTrue(game.gameExists("gameName"));
    }

    @Test
    public void addGameFail() throws DataAccessException{
        game.clear();
        game.addGame(2990, "goodGame");
        assertThrows(DataAccessException.class, () ->{
            game.addGame(2990, "badGame");
        });
    }

}
