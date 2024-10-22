package service;
import RequestandResponse.ClearResponse;
import dataaccess.*;

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
        return new ClearResponse("Success");
    }
}
