package service;
import dataaccess.*;
class ClearResponse {
    public ClearResponse(String response) {
        this.response = response;
    }
    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }
    private String response;
}
public class ClearService {
    public ClearResponse clearApplication() {
        GameDAO game = new GameDAO();
        UserDAO user = new UserDAO();
        AuthDAO auth = new AuthDAO();
        try {
            
        }
        catch (DataAccessException e) {
            return new ClearResponse(e.getMessage());
        }
        return new ClearResponse();
    }
}
