package passoff.server;
import model.*;
import dataaccess.*;
import org.junit.jupiter.api.*;
import RequestandResponse.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthTests {
    private static AuthDAO myAuth;

    @BeforeEach
    public void setup() throws DataAccessException, SQLException {
        myAuth = new AuthDAO();
        Connection conn = new DatabaseManager().getConnection();
        myAuth.clear(conn);
        conn.close();
    }

    @Test
    public void clearTest() {
        AuthData myCred = new AuthData("EarthboundRules4", "Jadenizer");
        try {
            myAuth.createToken(myCred.getAuth(), myCred.getName());
        }
        catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
        try {
            Connection conn = new DatabaseManager().getConnection();
            myAuth.clear(conn);
            conn.close();
        }
        catch (DataAccessException | SQLException exception) {
            throw new RuntimeException(exception);
        }
        AuthData retrievedToken = null;
        try {
            retrievedToken = myAuth.returnToken(myCred.getAuth());
        }
        catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
        assertEquals(null, retrievedToken);
    }
    @Test
    public void createAuthTest(){
        AuthData myCred =  new AuthData("EarthboundRules4", "Jadenizer");
        try{
            myAuth.createToken(myCred.getAuth(), myCred.getName());
        }
        catch(DataAccessException exception){
            throw new RuntimeException(exception);
        }
    }
    @Test
    public void unableCreateDitto(){
        AuthData myCred =  new AuthData("EarthboundRules4", "Jadenizer");
        try{
            myAuth.createToken(myCred.getAuth(), myCred.getName());
        }
        catch(DataAccessException exception){
            throw new RuntimeException(exception);
        }
        AuthData ditto = new AuthData("EarthboundRules4", "Jadenizer");
        assertThrows(DataAccessException.class, () -> {myAuth.createToken(ditto.getAuth(), ditto.getName());});
    }
}
