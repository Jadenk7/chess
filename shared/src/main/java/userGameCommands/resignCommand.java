package userGameCommands;
import websocket.commands.UserGameCommand;
public class resignCommand extends UserGameCommand {
    private Integer gameID;
    public resignCommand(String authToken, Integer gameID) {
        super(CommandType.RESIGN, authToken, gameID);
        this.gameID = gameID;
    }
    public void setGameID(int gameID) {
        this.gameID=gameID;
    }
    public Integer getGameID() {
        return gameID;
    }
}