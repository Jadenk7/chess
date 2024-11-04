package dataaccess;
import java.sql.SQLException;
import java.util.*;
import model.UserData;
import java.sql.Connection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class UserDAO {
    private DatabaseManager dbMan = new DatabaseManager();
    private static HashMap<String, UserData> userMap = new HashMap<>();
    public void createUser(String username, String password, String email) throws DataAccessException{
        Connection connection = dbMan.getConnection();
        try (var prepStatement = connection.prepareStatement("INSERT INTO user (username, password, email) VALUES(?, ?, ?)", RETURN_GENERATED_KEYS)) {
            prepStatement.setString(1, username);
            prepStatement.setString(2, password);
            prepStatement.setString(3, email);
            prepStatement.executeUpdate();
        }
        catch(SQLException exception){
            throw new DataAccessException(exception.getMessage());
        }
        finally{
            dbMan.closeConnection(connection);
        }
    }
    public UserData returnUser(String username) throws DataAccessException{
        Connection connection = dbMan.getConnection();
        try (var prepStatement = connection.prepareStatement("SELECT* FROM user WHERE username=?")) {
            prepStatement.setString(1, username);
            try (var rs = prepStatement.executeQuery()) {
                if (rs.next()) {
                    username = rs.getString("username");
                    var password = rs.getString("password");
                    var email = rs.getString("email");
                    return new UserData(username, password, email);
                }
                else{
                    return null;
                }
            }
        }
        catch(SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        finally{
            dbMan.closeConnection(connection);
        }
    }
    public void clear(Connection connection) throws DataAccessException{
        try (var prepStatement = connection.prepareStatement("DELETE FROM user")) {
            prepStatement.executeUpdate();
        }
        catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }
}
