package dataaccess;
import java.util.List;
import java.util.HashMap;
import model.GameData;
import model.AuthData;
import dataaccess.DataAccessException;
public class AuthDAO {
    private static HashMap<String, AuthData> auths = new HashMap<>();
    public HashMap<String, AuthData> getTokens(){

        return auths;
    }
    public void clear() throws DataAccessException{
        auths.clear();
    }
    public void createToken(AuthData t) throws DataAccessException{
        auths.put(t.getAuth(), t);
    }
    public AuthData readToken(String authToken) throws DataAccessException{
        return auths.get(authToken);
    }
    public void delete(String authToken) throws DataAccessException{
        auths.remove(authToken);
    }
}
