import chess.*;
import requestandresponse.*;
import ui.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import model.*;
public class Main {

    private static void loggedInCommands(ServerFacade server) throws IOException {
        System.out.println();
        displayLoggedInMenu();
        Scanner scanner = new Scanner(System.in);
        String command = scanner.next();
        scanner.nextLine();
        switch (command) {
            case "list":
                handleListGames(server);
                break;
            case "create":
                handleCreateGame(server);
                break;
            case "join":
                handleJoinGame(server);
                break;
            case "observe":
                handleObserveGame(server);
                break;
            case "help":
                loggedInCommands(server);
                break;
            case "logout":
                handleLogout(server);
                break;
            default:
                System.out.println("<Command not accepted>");
                loggedInCommands(server);
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

    private static void handleListGames(ServerFacade server) throws IOException {
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
        loggedInCommands(server);
    }
    private static void handleCreateGame(ServerFacade server) throws IOException {
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
        loggedInCommands(server);
    }

    private static void handleJoinGame(ServerFacade server) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ChessGame.TeamColor color = selectTeamColor(scanner);
        int id;
        try {
            System.out.println("Game ID: ");
            id = scanner.nextInt();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Input not accepted");
            scanner.nextLine();
            loggedInCommands(server);
            return;
        }
        JoinGameRequest request = new JoinGameRequest(color, id);
        JoinGameResponse response = server.joinGame(request);
        if (response.getMessage() != null) {
            System.out.println(response.getMessage());
            loggedInCommands(server);
        } else {
            System.out.println("<Successfully Joined Game>");
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            PrintBoard.drawForPlayer2(board);
            System.out.println();
            PrintBoard.drawForPlayer1(board);
            loggedInCommands(server);
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
    private static void handleObserveGame(ServerFacade server) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ChessGame.TeamColor color = ChessGame.TeamColor.SPECTATOR;
        int id;
        try {
            System.out.println("Game ID: ");
            id = scanner.nextInt();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Input not accepted");
            scanner.nextLine();
            loggedInCommands(server);
            return;
        }
        JoinGameRequest request = new JoinGameRequest(color, id);
        JoinGameResponse response = server.joinGame(request);
        if (response.getMessage() != null) {
            System.out.println(response.getMessage());
            loggedInCommands(server);
        } else {
            System.out.println("<Successfully Joined Game as Spectator>");
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            PrintBoard.drawForPlayer2(board);
            System.out.println();
            PrintBoard.drawForPlayer1(board);
            loggedInCommands(server);
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
    public static void main(String[] args) throws IOException {
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
                    loggedInCommands(server);
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
                    loggedInCommands(server);
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
    }