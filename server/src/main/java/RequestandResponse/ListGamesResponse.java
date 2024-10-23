package RequestandResponse;
import model.GameData;
import java.util.*;
public class ListGamesResponse {
    public ListGamesResponse() {}
    public ListGamesResponse(String message){
        this.message = message;
    }
    public ListGamesResponse(String message, Collection<GameData> gameList){
        this.gameList = gameList;
        this.message = message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public void setGameList(Collection<GameData> gameList) {
        this.gameList = gameList;
    }
    public Collection<GameData> getGameList() {
        return gameList;
    }
    private Collection<GameData> gameList;
    private String message;
}
