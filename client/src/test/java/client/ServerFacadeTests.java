package client;

import org.junit.jupiter.api.*;
import requestandresponse.RegRequest;
import requestandresponse.RegResponse;
import server.Server;
import service.ClearService;
import ui.ServerFacade;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void clear() {
        ClearService clearer = new ClearService();
        clearer.clearEverything();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void RegisterSuccess() {
        RegRequest request = new RegRequest("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
        try {
            RegResponse response = facade.register(request);
            assertNull(response.getMessage());
            assertNotNull(response.getAuth());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @Test
    void RegisterFail() {
        RegRequest request = new RegRequest("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
        try {
            facade.register(request);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        RegRequest request2 = new RegRequest("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
        try{
            RegResponse response = facade.register(request2);
            assertEquals("Error! Name already being used!", response.getMessage());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
