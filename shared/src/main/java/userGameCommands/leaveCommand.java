package userGameCommands;
import websocket.commands.UserGameCommand;
public class leaveCommand extends UserGameCommand {
    private Integer gameID;
    public leaveCommand(String authToken, Integer gameID) {
        super(CommandType.LEAVE, authToken, gameID);
        this.gameID=gameID;
    }
    public void setGameID(int gameID) {
        this.gameID=gameID;
    }
    public Integer getGameID() {
        return gameID;
    }
}
