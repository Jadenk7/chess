package userGameCommands;
import chess.ChessGame;
import websocket.commands.UserGameCommand;
public class connectCommand extends UserGameCommand {
    private ChessGame.TeamColor playerColor;
    private Integer gameID;
    public connectCommand(String authToken, ChessGame.TeamColor color, Integer gameID) {
        super(CommandType.CONNECT, authToken, gameID);
        this.playerColor=color;
        this.gameID=gameID;
    }
    public void setColor(ChessGame.TeamColor color) {
        this.playerColor = color;
    }
    public ChessGame.TeamColor getColor() {
        return playerColor;
    }
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
    public Integer getGameID() {
        return gameID;
    }
}
