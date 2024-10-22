package service;
import RequestandResponse.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import model.*;

public class ServiceUnitTests {
    private static final UserData me = new UserData("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
    @BeforeEach
    public void resetAll(){
        new ClearService().clearEverything();
    }
    @Test
    @Order(1)
    public void Clearing(){
        ClearService clearService = new ClearService();
        ClearResponse clearResponse = clearService.clearEverything();
        Assertions.assertEquals("Success", clearResponse.getResponse());
    }
    @Test
    @Order(2)
    public void RegisterSuccess(){
        RegRequest request = new RegRequest(me.getName(), me.getPassword(), me.getEmail());
        RegisterService service = new RegisterService();
        RegResponse response = service.registration(request);
        Assertions.assertEquals("Jadenizer", response.getName());
        Assertions.assertNotNull(response.getAuth());
        Assertions.assertNull(response.getMessage());
    }
    @Test
    @Order(3)
    public void RegisterNotEnoughInfo(){
        RegRequest request = new RegRequest(null, me.getPassword(), me.getEmail());
        RegisterService service = new RegisterService();
        RegResponse response = service.registration(request);
        Assertions.assertEquals("Error! Fill all required fields!", response.getMessage());
        Assertions.assertNull(response.getAuth());

    }
}
