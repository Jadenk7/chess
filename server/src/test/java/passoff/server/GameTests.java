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
}
