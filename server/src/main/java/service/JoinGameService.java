package service;
import chess.ChessGame;
import model.*;
import dataaccess.*;
import RequestandResponse.*;
public class JoinGameService {
    public JoinGameResponse joinGame(JoinGameRequest request, String authToken) {
        try {
            GameDAO gameDAO = new GameDAO();
            AuthDAO authDAO = new AuthDAO();
            JoinGameResponse joining = new JoinGameResponse();
            GameData game = new GameDAO().returnGame(request.getGameID());
            if (game != null){
                if(request.getPlayerColor() != null){
                    AuthData authtoken = authDAO.returnToken(authToken);
                    if(authtoken != null) {
                        if (game.getBlackUsername() != null && request.getPlayerColor() == ChessGame.TeamColor.BLACK) {
                            return new JoinGameResponse("Error! Color is taken");
                        }
                        if (game.getWhiteUsername() != null && request.getPlayerColor() == ChessGame.TeamColor.WHITE) {
                            return new JoinGameResponse("Error! Color is taken");
                        }
                        gameDAO.playerNamer(authtoken.getName(), request.getGameID(), request.getPlayerColor());
                    }
                    else{
                        return new JoinGameResponse("Error! Give auth Token");
                    }
                }
                else{
                    if(authDAO.returnToken(authToken) == null){
                        return new JoinGameResponse("Error! Give auth Token");
                    }
                    return new JoinGameResponse();
                }
            }
            else{
                return new JoinGameResponse("Error! No game");
            }
            return joining;
        }
        catch (Exception exception){
            return new JoinGameResponse(exception.getMessage());
        }
    }
}
