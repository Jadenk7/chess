package passoff.server;
import model.*;
import dataaccess.*;
import RequestandResponse.*;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;
import java.sql.Connection;
import java.sql.SQLException;

public class GameTests {
    private static GameDAO myGame;
    @BeforeEach
    public void setup() throws DataAccessException, SQLException {
        myGame = new GameDAO();
        Connection connection = new DatabaseManager().getConnection();
        myGame.clear(connection);
        connection.close();
    }
    @Test
    public void clearTest(){
        try {
            myGame.createGame("testerGame");
            Connection connection = new DatabaseManager().getConnection();
            myGame.clear(connection);
            connection.close();
            Collection<GameData> gameColl = myGame.returnGameMap();
            assertTrue(gameColl.isEmpty());
            assertNotNull(gameColl);
        }
        catch (DataAccessException | SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
    @Test
    public void createGameTest(){
        try{
            myGame.createGame("NameOGame");
        }
        catch(DataAccessException exception){
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void dittoGameTest(){
        try {
            myGame.createGame("NameOGame");
            Connection connection = new DatabaseManager().getConnection();
            myGame.clear(connection);
            connection.close();
            GameData ditto = new GameData("NameOGame");
            assertThrows(DataAccessException.class, () -> myGame.createGame(ditto.getGameName()));
        }
        catch (DataAccessException | SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
