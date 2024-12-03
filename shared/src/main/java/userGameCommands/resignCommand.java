package userGameCommands;
import websocket.commands.UserGameCommand;
public class resignCommand extends UserGameCommand {
    public resignCommand(String authToken, Integer gameID) {
        super(CommandType.RESIGN, authToken, gameID);
    }
}