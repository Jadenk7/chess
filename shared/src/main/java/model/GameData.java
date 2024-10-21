package model;
import chess.ChessGame;
import java.util.Objects;
public class GameData implements Comparable{
    private ChessGame game;
    private String gameName;
    private int gameID;
    private String blackName;
    private String whiteName;
    public void Game(int gameID, String blackName, String whiteName, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.game = game;
        this.blackName = blackName;
        this.whiteName = whiteName;
    }
    public int getID(){
        return gameID;
    }
    public void setID(int gameID){
        this.gameID = gameID;
    }
    public String getBlackName(){
        return blackName;
    }
    public void setBlackName(String blackName){
        this.blackName = blackName;
    }
    public String getWhiteName(){
        return whiteName;
    }
    public void setWhiteName(String whiteName){
        this.whiteName = whiteName;
    }
    public String getGameName(){
        return gameName;
    }
    public void setGameName(String gameName){
        this.gameName = gameName;
    }
    public ChessGame getChessGame(){
        return game;
    }
    public void setGame(ChessGame game){
        this.game = game;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameData gameData = (GameData) o;
        return gameID == gameData.gameID && Objects.equals(blackName, gameData.blackName) && Objects.equals(whiteName, gameData.whiteName) && Objects.equals(gameName, gameData.gameName) && Objects.equals(game, gameData.game);
    }
    @Override
    public int hashCode() {
        return Objects.hash(gameID, whiteName, blackName, gameName);
    }
    @Override
    public int compareTo(Object o) {
        return o.hashCode() - this.hashCode();
    }
}
