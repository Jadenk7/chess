package server.handler;
import com.google.gson.Gson;
import service.ClearService;
import service.*;
import spark.*;
public class ClearHandler implements Route{
    @Override
    public Object handle(Request request, Response response) {
        ClearService service = new ClearService();
        ClearResponse clearResponse = service.clearEverything();
        if(clearResponse.getResponse() == "Success"){
            response.status(200);
        }
        else{
            response.status(500);
        }
        return new Gson().toJson(clearResponse);
    }
}

