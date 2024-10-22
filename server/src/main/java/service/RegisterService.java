package service;
import java.util.UUID;
import RequestandResponse.RegResponse;
import RequestandResponse.RegRequest;
import dataaccess.*;
import model.*;
public class RegisterService {
    private AuthDAO auth = new AuthDAO();
    private UserDAO user = new UserDAO();
    public RegResponse registration(RegRequest req) {
        try {
            if(user.returnUser(req.getName()) != null){
                return new RegResponse("Error! Name already being used!");
            }
            if(req.getName() == null || req.getPassword() == null || req.getEmail() == null){
                return new RegResponse("Error! Fill all required fields!");
            }
            UserData thisUser = new UserData(req.getName(), req.getPassword(), req.getEmail());
            user.createUser(thisUser);
            String tok = UUID.randomUUID().toString();
            auth.createToken(new AuthData(tok, req.getName()));
            return new RegResponse(req.getName(), tok);
        }
        catch(Exception exception){
            return new RegResponse(exception.getMessage());
        }

    }
}
