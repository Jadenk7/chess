import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.ResponseException;
import requestandresponse.*;
import serverMessages.errorMessage;
import serverMessages.loadGameMessage;
import serverMessages.notificationMessage;
import ui.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.Scanner;
import model.*;
import webSocketClient.NotificationHandler;
import webSocketClient.WebSocketFacade;
import websocket.messages.ServerMessage;

import javax.websocket.DeploymentException;

public class Main implements NotificationHandler {
    private static int gameID;
    private static ChessBoard chessBoard = new ChessBoard(true);

    private static void loggedInCommands(ServerFacade server, int port) throws IOException, ResponseException, DeploymentException, URISyntaxException {
        System.out.println();
        displayLoggedInMenu();
        Scanner scanner = new Scanner(System.in);
        String command = scanner.next();
        scanner.nextLine();
        switch (command) {
            case "list":
                handleListGames(server, port);
                break;
            case "create":
                handleCreateGame(server, port);
                break;
            case "join":
                handleJoinGame(server, port);
                break;
            case "observe":
                handleObserveGame(server, port);
                break;
            case "help":
                loggedInCommands(server, port);
                break;
            case "logout":
                handleLogout(server);
                break;
            default:
                System.out.println("<Command not accepted>");
                loggedInCommands(server, port);
                break;
        }
    }
    private static void displayLoggedInMenu() {
        System.out.println("\"list\" - to list all games");
        System.out.println("\"create\" - to create and name a new game");
        System.out.println("\"join\" - to join a game and pick your color. ID required");
        System.out.println("\"observe\" - to observe a game. ID required");
        System.out.println("\"help\" - to list possible commands");
        System.out.println("\"logout\" - to log out");
    }

    private static void handleListGames(ServerFacade server, int port) throws IOException, ResponseException, DeploymentException, URISyntaxException {
        ListGamesResponse resp = server.listgames();
        if (resp.getMessage() != null) {
            System.out.println(resp.getMessage());
        } else {
            System.out.println("Current Games:");
            for (GameData game : resp.getGames()) {
                System.out.println();
                System.out.println(game.getGameName());
                System.out.println("Game ID: " + game.getID());
                System.out.println("White Player: " + game.getWhiteUsername());
                System.out.println("Black Player: " + game.getBlackUsername());
            }
        }
        loggedInCommands(server, port);
    }
    private static void handleCreateGame(ServerFacade server, int port) throws IOException, ResponseException, DeploymentException, URISyntaxException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Name of your new Game:");
        String name = scanner.nextLine();
        CreateGameRequest req = new CreateGameRequest(name);
        CreateGameResponse response = server.createGame(req);
        if (response.getMessage() != null) {
            System.out.println(response.getMessage());
        } else {
            System.out.println("<Successful Game Creation>");
            System.out.println("Game ID: " + response.getSequentialId());
            System.out.println();
        }
        loggedInCommands(server, port);
    }
    private static NotificationHandler notificationHandler = new Main();

    private static void handleJoinGame(ServerFacade server, int port) throws IOException, ResponseException, DeploymentException, URISyntaxException {
        Scanner scanner = new Scanner(System.in);
        ChessGame.TeamColor color = selectTeamColor(scanner);
        int id;
        try {
            System.out.println("Game ID: ");
            id = scanner.nextInt();
            gameID = id;
        } catch (java.util.InputMismatchException e) {
            System.out.println("Input not accepted");
            scanner.nextLine();
            loggedInCommands(server, port);
            return;
        }
        JoinGameRequest request = new JoinGameRequest(color, id);
        JoinGameResponse response = server.joinGame(request);
        if (response.getMessage() != null) {
            System.out.println(response.getMessage());
            loggedInCommands(server, port);
        } else {
            System.out.println("<Successfully Joined Game>");
            WebSocketFacade webSocketFacade = new WebSocketFacade("http://localhost:" + port, notificationHandler);
            webSocketFacade.connect(TokenPlaceholder.token, color, id);
            System.out.println("\n");
            gamePlay(server, port);
        }
    }
    private static ChessGame.TeamColor spectate(Scanner scanner) {
        ChessGame.TeamColor color = null;
        int validator = 0;
        while (validator == 0) {
            System.out.println("Type in: \"spectator\"");
            String selected = scanner.nextLine();
            if (selected.equals("spectator")) {
                color = ChessGame.TeamColor.SPECTATOR;
                validator++;
            }
        }
        return color;
    }
    private static ChessGame.TeamColor selectTeamColor(Scanner scanner) {
        ChessGame.TeamColor color = null;
        int validator = 0;
        while (validator == 0) {
            System.out.println("Select Team Color: \"black\" or \"white\"");
            String selected = scanner.nextLine();
            if (selected.equals("white")) {
                color = ChessGame.TeamColor.WHITE;
                validator++;
            } else if (selected.equals("black")) {
                color = ChessGame.TeamColor.BLACK;
                validator++;
            }
        }
        return color;
    }
    private static void handleObserveGame(ServerFacade server, int port) throws IOException, ResponseException, DeploymentException, URISyntaxException {
        Scanner scanner = new Scanner(System.in);
        ChessGame.TeamColor color = ChessGame.TeamColor.SPECTATOR;
        int id;
        try {
            System.out.println("Game ID: ");
            id = scanner.nextInt();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Input not accepted");
            scanner.nextLine();
            loggedInCommands(server, port);
            return;
        }
        JoinGameRequest request = new JoinGameRequest(color, id);
        JoinGameResponse response = server.joinGame(request);
        if (response.getMessage() != null) {
            System.out.println(response.getMessage());
            loggedInCommands(server, port);
        } else {
            System.out.println("<Successfully Joined Game as Spectator>");
            WebSocketFacade webSocketFacade = new WebSocketFacade("http://localhost:" + port, notificationHandler);
            webSocketFacade.connect(TokenPlaceholder.token, color, id);
            System.out.println("\n");
            gamePlay(server, port);
        }
    }
    private static void handleLogout(ServerFacade server) throws IOException {
        LogoutResponse response = server.logout();
        if (response.getMessage() != null) {
            System.out.println(response.getMessage());
        } else {
            System.out.println("<Successful Logout>");
            System.out.println();
            Main.listCommands();
        }
    }
    public static void listCommands() {
        System.out.println("Commands: ");
        System.out.println("\"register\" - to create your account");
        System.out.println("\"login\" - to log into your account");
        System.out.println("\"help\" - to list possible commands");
        System.out.println("\"quit\" - to quit your game");
    }
    public static void main(String[] args) throws IOException, ResponseException, DeploymentException, URISyntaxException {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream("server.properties")) {
            properties.load(in);
        }
        int port = Integer.parseInt(properties.getProperty("server.port"));
        ServerFacade server = new ServerFacade(port);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        Scanner scanner = new Scanner(System.in);
        System.out.println("♕ 240 Chess Client: " + piece);
        listCommands();
        while(true) {
            String input = scanner.nextLine();
            if (input.equals("register")) {
                System.out.println("New Username: ");
                String username = scanner.nextLine();
                System.out.println("New Password: ");
                String password = scanner.nextLine();
                System.out.println("Your Email Address: ");
                String email = scanner.nextLine();
                RegRequest req = new RegRequest(username, password, email);
                RegResponse resp = server.register(req);
                if(resp.getMessage() != null) {
                    System.out.println(resp.getMessage());
                    listCommands();
                }
                else{
                    TokenPlaceholder.token = resp.getAuth();
                    System.out.println("<Successful Registration>");
                    loggedInCommands(server, port);
                }
            }
            else if (input.equals("login")) {
                System.out.println("Username: ");
                String username = scanner.nextLine();
                System.out.println("Password: ");
                String password = scanner.nextLine();
                LoginRequest request = new LoginRequest(username, password);
                LoginResponse resp = server.login(request);
                if(resp.getMessage() != null){
                    System.out.println(resp.getMessage());
                    listCommands();
                }
                else{
                    TokenPlaceholder.token = resp.getAuth();
                    System.out.println("<Successful Login>");
                    loggedInCommands(server, port);
                }
            }
            else if (input.equals("help")) {
                System.out.println("\"register\" - to create your account");
                System.out.println("\"login\" - to log into your account");
                System.out.println("\"help\" - to list possible commands");
                System.out.println("\"quit\" - to quit your game");
            }
            else if (input.equals("quit")){
                System.out.println("<Program Terminated>");
                break;
            }
            else{
                System.out.println("<Command not accepted> ");
                listCommands();
            }
        }
    }
    private static void gamePlay(ServerFacade server, int port) throws ResponseException, DeploymentException, IOException, URISyntaxException {
        System.out.print("\n");
        System.out.println("\"help\" - for list of possible commands");
        System.out.println("\"redraw\" chess board");
        System.out.println("\"leave\" game");
        System.out.println("\"make\" move");
        System.out.println("\"resign\" game");
        System.out.println("\"highlight\" legal moves");
        WebSocketFacade webSocketFacade = new WebSocketFacade("http://localhost:" + port, notificationHandler);
        Scanner s = new Scanner(System.in);
        String command = s.next();
        s.nextLine();

        switch(command){
            case "help":
                gamePlay(server, port);
                break;
            case "redraw":
                PrintBoard.drawForPlayer2(chessBoard);
                System.out.println();
                PrintBoard.drawForPlayer1(chessBoard);
                gamePlay(server, port);
                break;
            case "leave":
                webSocketFacade.leave(TokenPlaceholder.token, gameID);
                loggedInCommands(server, port);
                break;
            case "resign":
                webSocketFacade.resign(TokenPlaceholder.token, gameID);
                loggedInCommands(server, port);
                break;
            case "highlight":
                loggedInCommands(server, port);
                break;
            case "make":
                String startPosition = "  ";
                if (startPosition.length() == 2) {
                    boolean validStart = false;
                    while (!validStart) {
                        System.out.println("Enter the starting position: (Ex. a1)");
                        startPosition = s.nextLine();
                        char startColumnLetter = startPosition.charAt(0);
                        int startColumnNumber = startColumnLetter - 'a';

                        int startRowNumber;
                        try {
                            startRowNumber = Character.getNumericValue(startPosition.charAt(1)) - 1;
                            switch(startColumnNumber){
                                case 0:
                                    startColumnNumber = 7;
                                    break;
                                case 1:
                                    startColumnNumber = 6;
                                    break;
                                case 2:
                                    startColumnNumber = 5;
                                    break;
                                case 3:
                                    startColumnNumber = 4;
                                    break;
                                case 4:
                                    startColumnNumber = 3;
                                    break;
                                case 5:
                                    startColumnNumber = 2;
                                    break;
                                case 6:
                                    startColumnNumber = 1;
                                    break;
                                case 7:
                                    startColumnNumber = 0;
                                    break;
                            }
                            if (startColumnNumber >= 0 && startColumnNumber <= 7 && startRowNumber >= 0 && startRowNumber <= 7) {
                                System.out.println("Enter the end position: (Ex. a1)");
                                String endPosition = s.nextLine();
                                if (endPosition.length() == 2) {
                                    char endColumnLetter = endPosition.charAt(0);
                                    int endColumnNumber = endColumnLetter - 'a';
                                    int endRowNumber;
                                    try {
                                        endRowNumber = Character.getNumericValue(endPosition.charAt(1)) -1;
                                        switch(endColumnNumber){
                                            case 0:
                                                endColumnNumber = 7;
                                                break;
                                            case 1:
                                                endColumnNumber = 6;
                                                break;
                                            case 2:
                                                endColumnNumber = 5;
                                                break;
                                            case 3:
                                                endColumnNumber = 4;
                                                break;
                                            case 4:
                                                endColumnNumber = 3;
                                                break;
                                            case 5:
                                                endColumnNumber = 2;
                                                break;
                                            case 6:
                                                endColumnNumber = 1;
                                                break;
                                            case 7:
                                                endColumnNumber = 0;
                                                break;
                                        }
                                        if (endColumnNumber >= 0 && endColumnNumber <= 7 && endRowNumber >= 0 && endRowNumber <= 7) {
                                            ChessPosition startingPosition = new ChessPosition(startRowNumber, startColumnNumber);
                                            ChessPosition endingPosition = new ChessPosition(endRowNumber, endColumnNumber);
                                            ChessMove move = new ChessMove(startingPosition, endingPosition, null);
                                            webSocketFacade.makeMove(TokenPlaceholder.token, gameID, move);
                                        } else {
                                            System.out.println("Invalid end position. Column and row must be in range (a-h) and (1-8).");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid end row number. Please enter a number (1-8).");
                                        gamePlay(server,port);
                                    }
                                } else {
                                    System.out.println("Invalid input for end position. Please enter a valid position. 1. (a-h) 2. (1-8)");
                                }

                                validStart = true;
                            } else {
                                System.out.println("Invalid starting position. Column and row must be in range (a-h) and (1-8).");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid starting row number. Please enter a number (1-8).");
                            gamePlay(server, port);
                        }
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid position. 1. (a-h) 2. (1-8)");
                }
                gamePlay(server, port);
                break;
            default:
                System.out.println("Invalid Command");
                gamePlay(server, port);
                break;
        }
    }
    @Override
    public void notify(String message) {
        try{
            ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
            switch(serverMessage.getServerMessageType()) {
                case ERROR:
                    errorMessage error_message=new Gson().fromJson(message, errorMessage.class);
                    System.out.println(error_message.getErrorMessage());
                    break;
                case NOTIFICATION:
                    notificationMessage notification_message=new Gson().fromJson(message, notificationMessage.class);
                    System.out.println(notification_message.getMessage());
                    break;
                case LOAD_GAME:
                    loadGameMessage load_game = new Gson().fromJson(message, loadGameMessage.class);
                    chessBoard = load_game.getGame().getChessGame().getBoard();
                    PrintBoard.drawForPlayer2(chessBoard);
                    System.out.println();
                    PrintBoard.drawForPlayer1(chessBoard);
                    System.out.print("\n");
                    break;
            }
        }
        catch(RuntimeException e){
            e.printStackTrace();
        }
    }
    }