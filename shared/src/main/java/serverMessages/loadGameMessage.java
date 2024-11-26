package serverMessages;
import model.GameData;
import websocket.messages.ServerMessage;
public class loadGameMessage extends ServerMessage {
    GameData game;
    public loadGameMessage(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game=game;
    }
    public void setGame(GameData game) {
        this.game = game;
    }
    public GameData getGame() {
        return game;
    }
}