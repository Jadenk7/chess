import chess.*;
import com.google.gson.Gson;
import exception.ResponseException;
import requestandresponse.*;
import servermessages.ErrorMessage;
import servermessages.LoadGameMessage;
import servermessages.NotificationMessage;
import ui.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.Scanner;
import model.*;
import websocketclient.NotificationHandler;
import websocketclient.WebSocketFacade;
import websocket.messages.ServerMessage;

import javax.websocket.DeploymentException;

public class Main implements NotificationHandler {
    private static int gameID;
    private static ChessBoard chessBoard = new ChessBoard(true);

    private static void loggedInCommands(ServerFacade server, int port) throws IOException, ResponseException, DeploymentException,
            URISyntaxException {
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

    private static void handleListGames(ServerFacade server, int port) throws IOException, ResponseException, DeploymentException,
            URISyntaxException {
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
    private static void handleCreateGame(ServerFacade server, int port) throws IOException, ResponseException, DeploymentException,
            URISyntaxException {
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
    private static void handleObserveGame(ServerFacade server, int port) throws IOException, ResponseException, DeploymentException,
            URISyntaxException {
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
                        char startColLetter = startPosition.charAt(0);
                        int startColNumber = startColLetter - 'a';

                        int startRowNumber;
                        try {
                            startRowNumber = Character.getNumericValue(startPosition.charAt(1)) - 1;
                            switch(startColNumber){
                                case 0:
                                    startColNumber = 7;
                                    break;
                                case 1:
                                    startColNumber = 6;
                                    break;
                                case 2:
                                    startColNumber = 5;
                                    break;
                                case 3:
                                    startColNumber = 4;
                                    break;
                                case 4:
                                    startColNumber = 3;
                                    break;
                                case 5:
                                    startColNumber = 2;
                                    break;
                                case 6:
                                    startColNumber = 1;
                                    break;
                                case 7:
                                    startColNumber = 0;
                                    break;
                            }
                            if (startRowNumber >= 0 && startRowNumber <= 7 && startColNumber >= 0 && startColNumber <= 7) {
                                System.out.println("Enter the end position: (Ex. a1)");
                                String endPos = s.nextLine();
                                if (endPos.length() == 2) {
                                    char endColLetter = endPos.charAt(0);
                                    int endColNumber = endColLetter - 'a';
                                    int endRowNumber;
                                    try {
                                        endRowNumber = Character.getNumericValue(endPos.charAt(1)) -1;
                                        switch(endColNumber){
                                            case 0:
                                                endColNumber = 7;
                                                break;
                                            case 1:
                                                endColNumber = 6;
                                                break;
                                            case 2:
                                                endColNumber = 5;
                                                break;
                                            case 3:
                                                endColNumber = 4;
                                                break;
                                            case 4:
                                                endColNumber = 3;
                                                break;
                                            case 5:
                                                endColNumber = 2;
                                                break;
                                            case 6:
                                                endColNumber = 1;
                                                break;
                                            case 7:
                                                endColNumber = 0;
                                                break;
                                        }
                                        if (endColNumber >= 0 && endColNumber <= 7 && endRowNumber >= 0 && endRowNumber <= 7) {
                                            ChessPosition startingPosition = new ChessPosition(startRowNumber, startColNumber);
                                            ChessPosition endingPosition = new ChessPosition(endRowNumber, endColNumber);
                                            ChessMove move = new ChessMove(startingPosition, endingPosition, null);
                                            webSocketFacade.makeMove(TokenPlaceholder.token, gameID, move);
                                        } else {
                                            System.out.println("Invalid end position. Enter column (a-h) and row (1-8).");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid end row number. Must be 1-8.");
                                        gamePlay(server,port);
                                    }
                                } else {
                                    System.out.println("Invalid end position. Enter a valid position: (a-h)(1-8)");
                                }

                                validStart = true;
                            } else {
                                System.out.println("Invalid starting position. Enter column (a-h) and row (1-8).");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid starting row number. Must be 1-8.");
                            gamePlay(server, port);
                        }
                    }
                } else {
                    System.out.println("Invalid input. Enter a valid position: (a-h)(1-8)");
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
                    ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                    System.out.println(errorMessage.getErrorMessage());
                    break;
                case NOTIFICATION:
                    NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                    System.out.println(notificationMessage.getMessage());
                    break;
                case LOAD_GAME:
                    LoadGameMessage loadGame = new Gson().fromJson(message, LoadGameMessage.class);
                    chessBoard = loadGame.getGame().getChessGame().getBoard();
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