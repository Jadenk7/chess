package dataaccess;
import model.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;


public class UserTests {

    private static UserDAO myUser;

    @BeforeEach
    public void setup() throws DataAccessException, SQLException {
        myUser = new UserDAO();
        Connection connection = new DatabaseManager().getConnection();
        myUser.clear(connection);
        connection.close();
    }
    @Test
    public void clearTest(){
        UserData jadenCred = new UserData("Jadenizer" , "Earthbounder4", "kunzlerj9@gmail.com");
        try {
            myUser.createUser(jadenCred.getName(), jadenCred.getPassword(), jadenCred.getEmail());
        }
        catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
        try {
            Connection connection = new DatabaseManager().getConnection();
            myUser.clear(connection);
            connection.close();
        }
        catch (DataAccessException | SQLException exception) {
            throw new RuntimeException(exception);
        }
        UserData placeholder = null;
        try {
            placeholder = myUser.returnUser(jadenCred.getName());
        }
        catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
        assertEquals(null, placeholder);
    }
    @Test
    public void userCreationTest(){
        UserData jadenCred =  new UserData("Jadenizer" , "Earthbounder4", "kunzlerj9@gmail.com");
        try{
            myUser.createUser(jadenCred.getName(), jadenCred.getPassword(), jadenCred.getEmail());
        }
        catch(DataAccessException exception){
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void dittoUsernameTest(){
        UserData jadenCred =  new UserData("Jadenizer" , "Earthbounder4", "kunzlerj9@gmail.com");
        try{
            myUser.createUser(jadenCred.getName(), jadenCred.getPassword(), jadenCred.getEmail());
        }
        catch(DataAccessException exception){
            throw new RuntimeException(exception);
        }
        UserData dittoUsername = new UserData("Jadenizer", "EarthboundRules", "jadenizer@gmail.com");
        assertThrows(DataAccessException.class, () ->
        {myUser.createUser(dittoUsername.getName(), dittoUsername.getPassword(), dittoUsername.getEmail());});
    }
    @Test
    public void returnUserTest(){
        UserData jadenCred =  new UserData("Jadenizer" , "Earthbounder4", "kunzlerj9@gmail.com");
        try {
            myUser.createUser(jadenCred.getName(), jadenCred.getPassword(), jadenCred.getEmail());
            UserData thisUser = myUser.returnUser(jadenCred.getName());
            assertNotNull(thisUser, "null");
            assertEquals(jadenCred.getName(), thisUser.getName(), "Usernames unmatched");
            assertEquals(jadenCred.getPassword(), thisUser.getPassword(), "Passwords unmatched");
            assertEquals(jadenCred.getEmail(), thisUser.getEmail(), "Emails unmatched");
        }
        catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void returnUserNonTest(){
        UserData jadenCred =  new UserData("Jadenizer" , "Earthbounder4", "kunzlerj9@gmail.com");
        try {
            myUser.createUser(jadenCred.getName(), jadenCred.getPassword(), jadenCred.getEmail());
        }
        catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
        UserData placeholder = null;
        try {
            placeholder = myUser.returnUser(jadenCred.getName());
        }
        catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
        assertEquals(jadenCred.getName(), placeholder.getName());
    }
}
