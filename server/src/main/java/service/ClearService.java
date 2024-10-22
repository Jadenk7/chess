package service;
import dataaccess.*;
class ClearResponse {
    public ClearResponse(String response) {
        this.response = response;
    }
    public String getResponse() {
        return response;
    }
    private String response;
}
public class ClearService {
    public ClearResponse clearEverything() {
        GameDAO game = new GameDAO();
        UserDAO user = new UserDAO();
        AuthDAO auth = new AuthDAO();
        try {
            game.clear();
            user.clear();
            auth.clear();
        }
        catch (DataAccessException exception) {
            return new ClearResponse(exception.getMessage());
        }
        return new ClearResponse("");
    }
}
