package serverMessages;
import websocket.messages.ServerMessage;
public class notificationMessage extends ServerMessage {
    private String message;
    public notificationMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message=message;
    }
    public void setMessage(String message) {
        this.message=message;
    }
    public String getMessage() {
        return message;
    }
}
