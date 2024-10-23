package service;
import  dataaccess.*;
import model.*;
import RequestandResponse.LoginResponse;
import RequestandResponse.LoginRequest;
import java.util.UUID;
public class LoginService {
    private AuthDAO authDAO = new AuthDAO();
    private UserDAO userDAO = new UserDAO();
    public LoginResponse logger(LoginRequest request) {
        try{
            if(request.getName() != null && request.getPassword() != null){
                UserData thisUser = userDAO.returnUser(request.getName());
                if (thisUser == null){
                    return new LoginResponse("Error! Wrong password");
                }
                if (thisUser.getPassword().equals(request.getPassword())) {
                    String Tokenize = UUID.randomUUID().toString();
                    authDAO.createToken(new AuthData(Tokenize, request.getName()));
                    return new LoginResponse(request.getName(), Tokenize);
                }
                else{
                    return new LoginResponse("Error! Wrong password");
                }
            }
            else{
                return new LoginResponse();
            }
        }
        catch(Exception exception){
            return new LoginResponse(exception.getMessage());
        }
    }
}
