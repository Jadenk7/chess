package requestandresponse;
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
    public void setID(int id){
        this.gameID = id;
    }
    public int getID() {
        return gameID;
    }
    private String message;
    private Integer gameID;
}
