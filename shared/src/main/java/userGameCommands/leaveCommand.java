package userGameCommands;
import websocket.commands.UserGameCommand;
public class leaveCommand extends UserGameCommand {
    public leaveCommand(String authToken, Integer gameID) {
        super(CommandType.LEAVE, authToken, gameID);
    }
}
