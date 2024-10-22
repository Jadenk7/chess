package RequestandResponse;
public class CreateGameRequest {
    public CreateGameRequest(String name){
        this.name = name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    private String name;
}
