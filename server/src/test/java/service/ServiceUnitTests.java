package service;
import RequestandResponse.ClearResponse;
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
}
