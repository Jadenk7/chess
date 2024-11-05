package service;
import RequestandResponse.*;
import org.junit.jupiter.api.*;
import model.*;
import java.util.*;
import chess.ChessGame;
import server.Server;

public class ServiceUnitTests {
    private static Server server;
    private static final UserData me = new UserData("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
    @BeforeEach
    public void resetAll(){
        new ClearService().clearEverything();
    }
    @BeforeAll
    public static void setServer(){
        server = new Server();
        server.run(8080);
    }

    @AfterAll
    static void stopServer(){
        server.stop();
    }

    @Test
    @Order(1)
    public void Clearing(){
        ClearService clearService = new ClearService();
        ClearResponse clearResponse = clearService.clearEverything();
        Assertions.assertEquals(null, clearResponse.getResponse());
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
    public void RegisterNotEnoughInfo() {
        RegRequest request = new RegRequest(null, me.getPassword(), me.getEmail());
        RegisterService service = new RegisterService();
        RegResponse response = service.registration(request);
        Assertions.assertEquals("Error! Fill all required fields!", response.getMessage());
        Assertions.assertNull(response.getAuth());
    }

    @Test
    @Order(4)
    public void CreationSuccess() {
        GameData game = new GameData(500, null, null, "TheBestGame", null);
        RegRequest request = new RegRequest(me.getName(), me.getPassword(), me.getEmail());
        RegisterService service = new RegisterService();
        RegResponse response = service.registration(request);
        CreateGameService gameServ = new CreateGameService();
        CreateGameRequest gameReq = new CreateGameRequest("OtherGame");
        CreateGameResponse gameResp = gameServ.gameCreator(gameReq, response.getAuth());
        Assertions.assertNotNull(game.getID());
        Assertions.assertNull(gameResp.getMessage());
        Assertions.assertNotNull(gameReq.getName());
    }

    @Test
    @Order(5)
    public void CreationFailure() {
        RegRequest request = new RegRequest(me.getName(), me.getPassword(), me.getEmail());
        RegisterService service = new RegisterService();
        RegResponse response = service.registration(request);
        CreateGameService gameServ = new CreateGameService();
        CreateGameRequest gameReq = new CreateGameRequest(null);
        CreateGameResponse gameResp = gameServ.gameCreator(gameReq, response.getAuth());
        Assertions.assertNotNull(response.getAuth());
        Assertions.assertEquals("Error! No name", gameResp.getMessage());
    }
    @Test
    @Order(6)
    public void JoinSuccess(){
        RegRequest request = new RegRequest(me.getName(), me.getPassword(), me.getEmail());
        RegisterService service = new RegisterService();
        RegResponse response = service.registration(request);
        CreateGameService createService = new CreateGameService();
        CreateGameRequest createRequest = new CreateGameRequest("Game!");
        CreateGameResponse createResponse = createService.gameCreator(createRequest, response.getAuth());
        JoinGameRequest blackRequest = new JoinGameRequest(ChessGame.TeamColor.BLACK, createResponse.getID());
        JoinGameRequest whiteRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, createResponse.getID());
        JoinGameService joiner = new JoinGameService();
        JoinGameResponse blackResponse = joiner.joinGame(blackRequest, response.getAuth());
        JoinGameResponse whiteResponse = joiner.joinGame(whiteRequest, response.getAuth());
        Assertions.assertNull(blackResponse.getMessage());
        Assertions.assertNull(whiteResponse.getMessage());
    }
    @Test
    @Order(7)
    public void JoinFailure(){
        RegRequest request = new RegRequest(me.getName(), me.getPassword(), me.getEmail());
        RegisterService service = new RegisterService();
        RegResponse response = service.registration(request);
        CreateGameService creationService = new CreateGameService();
        CreateGameRequest creationRequest = new CreateGameRequest("Game!");
        CreateGameResponse creationResponse = creationService.gameCreator(creationRequest, response.getAuth());
        JoinGameRequest otherBlackRequest = new JoinGameRequest(ChessGame.TeamColor.BLACK, creationResponse.getID());
        JoinGameRequest blackRequest = new JoinGameRequest(ChessGame.TeamColor.BLACK, creationResponse.getID());
        JoinGameRequest otherWhiteRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, creationResponse.getID());
        JoinGameRequest whiteRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, creationResponse.getID());
        JoinGameService joiner = new JoinGameService();
        JoinGameResponse otherBlackResponse = joiner.joinGame(otherBlackRequest, response.getAuth());
        JoinGameResponse blackResponse = joiner.joinGame(blackRequest, response.getAuth());
        JoinGameResponse otherWhiteResponse = joiner.joinGame(otherWhiteRequest, response.getAuth());
        JoinGameResponse whiteResponse = joiner.joinGame(whiteRequest, response.getAuth());
        Assertions.assertEquals("Error! Color is taken", blackResponse.getMessage());
        Assertions.assertEquals("Error! Color is taken", whiteResponse.getMessage());
    }
    @Test
    @Order(8)
    public void LoginSuccess(){
        RegRequest request = new RegRequest(me.getName(), me.getPassword(), me.getEmail());
        RegisterService service = new RegisterService();
        RegResponse response = service.registration(request);
        LoginService loginServ = new LoginService();
        LoginRequest loginReq = new LoginRequest(me.getName(), me.getPassword());
        LoginResponse loginResp = loginServ.logger(loginReq);
        Assertions.assertNull(loginResp.getMessage());
    }

    @Test
    @Order(9)
    public void WrongPasswordLogin(){
        RegRequest request = new RegRequest(me.getName(), "Uhhhhh I Don't Know The Password", me.getEmail());
        RegisterService service = new RegisterService();
        RegResponse response = service.registration(request);
        LoginService loginServ = new LoginService();
        LoginRequest loginReq = new LoginRequest(me.getName(), me.getPassword());
        LoginResponse loginResp = loginServ.logger(loginReq);
        Assertions.assertEquals("Error! Wrong password", loginResp.getMessage());
    }
    @Test
    @Order(10)
    public void LoggedOut(){
        RegRequest request = new RegRequest(me.getName(), me.getPassword(), me.getEmail());
        RegisterService service = new RegisterService();
        RegResponse response = service.registration(request);
        LoginService loginServ = new LoginService();
        LoginRequest loginReq = new LoginRequest(me.getName(), me.getPassword());
        LoginResponse loginResp = loginServ.logger(loginReq);
        LogoutService logoutServ = new LogoutService();
        LogoutResponse logoutResp = logoutServ.logout(response.getAuth());
        Assertions.assertNull(logoutResp.getMessage());
    }

    @Test
    @Order(11)
    public void LogoutAuthEmpty(){
        RegRequest request = new RegRequest(me.getName(), me.getPassword(), me.getEmail());
        RegisterService service = new RegisterService();
        RegResponse response = service.registration(request);
        LoginRequest loginRequest = new LoginRequest(me.getName(), me.getPassword());
        LoginService loginService = new LoginService();
        LoginResponse loginResp = loginService.logger(loginRequest);
        LogoutService logoutServ = new LogoutService();
        LogoutResponse logoutResp = logoutServ.logout(null);
        Assertions.assertEquals("Error! No auth token", logoutResp.getMessage());
    }
    @Test
    @Order(12)
    public void ListGamesSuccess(){
        RegRequest request = new RegRequest(me.getName(), me.getPassword(), me.getEmail());
        RegisterService service = new RegisterService();
        RegResponse response = service.registration(request);
        CreateGameService game1Serv = new CreateGameService();
        CreateGameRequest game1Req = new CreateGameRequest("Jaden");
        CreateGameService game2Serv = new CreateGameService();
        CreateGameRequest game2Req = new CreateGameRequest("Kunzler");
        ListGamesService gameListService = new ListGamesService();
        ListGamesResponse gameListResponse = gameListService.listGames(response.getAuth());
        ArrayList<GameData> gameList1 = new ArrayList<>(gameListResponse.getGames());
        ArrayList<GameData> gameList2 = new ArrayList<>();
        GameData otherGame = new GameData(1, null, null, "Jaden", null);
        GameData otherOtherGame = new GameData(2, null, null, "Kunzler", null);
        gameList2.add(otherGame);
        gameList2.add(otherOtherGame);
        Assertions.assertNull(gameListResponse.getMessage());
    }

    @Test
    @Order(13)
    public void ListGamesWithoutAuth(){
        RegRequest request = new RegRequest(me.getName(), me.getPassword(), me.getEmail());
        RegisterService service = new RegisterService();
        RegResponse response = service.registration(request);
        CreateGameService game1Serv = new CreateGameService();
        CreateGameRequest game1Req = new CreateGameRequest("Jaden");
        CreateGameService game2Serv = new CreateGameService();
        CreateGameRequest game2Req = new CreateGameRequest("Kunzler");
        ListGamesService gameListService = new ListGamesService();
        ListGamesResponse gameListResponse = gameListService.listGames(response.getAuth()+1);
        Assertions.assertEquals("Error! No auth token", gameListResponse.getMessage());
    }
}
