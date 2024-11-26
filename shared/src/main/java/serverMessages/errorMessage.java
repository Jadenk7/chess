package serverMessages;
import websocket.messages.ServerMessage;
public class errorMessage extends ServerMessage{
    private String errorMessage;
    public errorMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage=errorMessage;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
