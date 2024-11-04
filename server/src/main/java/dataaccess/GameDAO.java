package dataaccess;
import chess.ChessGame;
import java.util.*;
import java.lang.*;
import model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import chess.ChessBoard;
import chess.ChessPiece;
import java.sql.Connection;
import java.sql.SQLException;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
public class GameDAO {
    private static HashMap<Integer, GameData> gameMap = new HashMap<>();
    private static int idInstancer = 1;

    private DatabaseManager dbMan = new DatabaseManager();

    public void clear(Connection connection) throws DataAccessException {
        try (var prepStatement = connection.prepareStatement("DELETE FROM game")) {
            prepStatement.executeUpdate();
        }
        catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }
    public int createGame(String gameName) throws DataAccessException {
        Connection connection = dbMan.getConnection();
        try (var prepStatement = connection.prepareStatement("INSERT INTO game (gameName, game) VALUES(?, ?)", RETURN_GENERATED_KEYS)) {
            prepStatement.setString(1, gameName);
            prepStatement.setString(2,  new Gson().toJson(new ChessGame()));
            prepStatement.executeUpdate();
            var gameID = 0;
            var resSet = prepStatement.getGeneratedKeys();
            if (resSet.next()) {
                gameID = resSet.getInt(1);
            }
            return gameID;
        }
        catch(SQLException exception){
            throw new DataAccessException(exception.getMessage());
        }
        finally{
            dbMan.closeConnection(connection);
        }
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
    public void playerNamer(String username, int gameID, ChessGame.TeamColor color) throws DataAccessException {
        GameData game = gameMap.get(gameID);
        if (color == ChessGame.TeamColor.WHITE) {
            game.setWhiteUsername(username);
        } else {
            game.setBlackUsername(username);
        }

    }
    public void delete(int gameID) throws DataAccessException{
        gameMap.remove(gameID);
    }


}
