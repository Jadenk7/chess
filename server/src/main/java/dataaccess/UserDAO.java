package dataaccess;
import java.util.*;
import model.UserData;
public class UserDAO {
    private static HashMap<String, UserData> userMap = new HashMap<>();
    public void createUser(UserData user) throws DataAccessException{
        userMap.put(user.getName(), user);
    }
    public UserData returnUser(String username) throws DataAccessException{
        return userMap.get(username);
    }
    public void clear() throws DataAccessException{
        userMap.clear();
    }
}
