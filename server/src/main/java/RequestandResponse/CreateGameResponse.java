package RequestandResponse;
public class CreateGameResponse {
    public CreateGameResponse(String message){
        this.message = message;
    }
    public CreateGameResponse(int ID) {
        this.ID = ID;
    }
    public CreateGameResponse(String message, int ID){
        this.message = message;
        this.ID = ID;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public void setID(int ID){
        this.ID = ID;
    }
    public int getID() {
        return ID;
    }
    private String message;
    private Integer ID;
}
