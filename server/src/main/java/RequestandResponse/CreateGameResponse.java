package RequestandResponse;
public class CreateGameResponse {
    public CreateGameResponse(String message){
        this.message = message;
    }
    public CreateGameResponse(int gameID) {
        this.gameID = gameID;
    }
    public CreateGameResponse(String message, int gameID){
        this.message = message;
        this.gameID = gameID;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public void setID(int ID){
        this.gameID = ID;
    }
    public int getID() {
        return gameID;
    }
    private String message;
    private Integer gameID;
}
