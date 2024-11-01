package dataaccess;
import java.util.*;
import model.AuthData;
import java.sql.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class AuthDAO {
    private static HashMap<String, AuthData> authMap = new HashMap<>();
    private DatabaseManager dbMan = new DatabaseManager();

    public void createToken(AuthData auth) throws DataAccessException{
        authMap.put(auth.getAuth(), auth);
    }
    public AuthData returnToken(String authToken) throws DataAccessException{
        return authMap.get(authToken);
    }
    public HashMap<String, AuthData> getTokens(){
        return authMap;
    }
    public void clear() throws DataAccessException{
        authMap.clear();
    }
    public void delete(String authToken) throws DataAccessException{
        authMap.remove(authToken);
    }
}
