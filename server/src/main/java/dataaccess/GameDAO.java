package dataaccess;
import chess.ChessGame;
import java.util.*;
import java.lang.*;
import model.*;

public class GameDAO {
    private static HashMap<Integer, GameData> gameMap = new HashMap<>();
    private static int idInstancer = 1;
    public int createGame(GameData game) throws DataAccessException{
        game.setID(idInstancer);
        idInstancer += 1;
        gameMap.put(idInstancer, game);
        return idInstancer;
    }
    public GameData returnGame(int gameID) throws DataAccessException{
        return gameMap.get(gameID);
    }
    public Collection<GameData> returnGameMap() throws DataAccessException{
        return gameMap.values();
    }
    public void updateGame(GameData game) throws DataAccessException{
        int thisID = game.getID();
        if(gameMap.containsKey(thisID)){
            gameMap.put(thisID, game);
        }
    }
    public void playerNamer(String username, int gameID, ChessGame.TeamColor color) throws DataAccessException{
        GameData game = gameMap.get(gameID);
        if(color == ChessGame.TeamColor.WHITE){
            game.setWhiteName(username);
        }
        else{
            game.setBlackName(username);
        }

    }
    public void clear() throws DataAccessException{
        gameMap.clear();
    }
    public void delete(int gameID) throws DataAccessException{
        gameMap.remove(gameID);
    }


}
