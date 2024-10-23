package RequestandResponse;
import chess.ChessGame;
public class JoinGameRequest {
    public JoinGameRequest(ChessGame.TeamColor colorOfPlayer, int gameID){
        this.colorOfPlayer = colorOfPlayer;
        this.gameID = gameID;
    }
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
    public int getGameID() {
        return gameID;
    }
    public void setPlayerColor(ChessGame.TeamColor colorOfPlayer) {
        this.colorOfPlayer = colorOfPlayer;
    }
    public ChessGame.TeamColor getPlayerColor() {
        return colorOfPlayer;
    }
    private ChessGame.TeamColor colorOfPlayer;
    private int gameID;
}
