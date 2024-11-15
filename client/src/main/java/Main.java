import chess.*;
import requestandresponse.*;
import server.Server;
import ui.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import model.*;
public class Main {

    private static void loggedInCommands(ServerFacade server) throws IOException {
        System.out.print("\n");
        System.out.println("\"list\" - to list all games");
        System.out.println("\"create\" - to create and name a new game");
        System.out.println("\"join\" - to join a game and pick your color. ID required");
        System.out.println("\"observe\" - to observe a game. ID required");
        System.out.println("\"help\" - to list possible commands");
        System.out.println("\"logout\" - to log out");
        System.out.println("\"quit\" - to quit your game");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.next();
        scanner.nextLine();
        switch(command){
            case "list":
                ListGamesResponse resp = server.listgames();
                if(resp.getMessage() != null){
                    System.out.println(resp.getMessage());
                    loggedInCommands(server);
                    break;
                }
                else{
                    System.out.println("Current Games:");
                    for(GameData game: resp.getGames()){
                        System.out.println();
                        System.out.println(game.getGameName());
                        System.out.println("Game ID: " + game.getID());
                        System.out.println("White Player: " + game.getWhiteUsername());
                        System.out.println(" Black Player: " + game.getBlackUsername());
                    }
                    loggedInCommands(server);
                    break;
                }
            case "create":
                System.out.println("Name of your new Game:");
                String name = scanner.nextLine();
                CreateGameRequest req = new CreateGameRequest(name);
                CreateGameResponse response = server.createGame(req);
                if(response.getMessage() != null) {
                    System.out.println(response.getMessage());
                    loggedInCommands(server);
                    break;
                }
                else{
                    System.out.println("<Successful Game Creation>");
                    System.out.println("Game ID: " + response.getID());
                    System.out.println();
                    loggedInCommands(server);
                    break;
                }
            case "join":
                ChessGame.TeamColor color = null;
                int validator = 0;
                while(validator == 0) {
                    System.out.println("Select Team Color: \"black\" or \"white\"");
                    String selected = scanner.nextLine();
                    if (selected.equals("white")) {
                        color = ChessGame.TeamColor.WHITE;
                        validator += 1;
                    }
                    if (selected.equals("black")) {
                        color = ChessGame.TeamColor.BLACK;
                        validator += 1;
                    }
                }
                int id;
                try {
                    System.out.println("Game ID: ");
                    id=scanner.nextInt();
                }
                catch (java.util.InputMismatchException e) {
                    System.out.println("Input not accepted");
                    scanner.nextLine();
                    loggedInCommands(server);
                    break;
                }
                JoinGameRequest request = new JoinGameRequest(color, id);
                JoinGameResponse myResponse = server.joinGame(request);
                if (myResponse.getMessage() != null) {
                    System.out.println(myResponse.getMessage());
                    loggedInCommands(server);
                    break;
                }
                else {
                    System.out.println("<Successfully Joined Game>");
                    ChessBoard board = new ChessBoard();
                    board.resetBoard();
                    PrintBoard.drawForPlayer1(board);
                    System.out.print("\n");
                    PrintBoard.drawForPlayer2(board);
                    break;
                }
            case "observe":
                int gameID;
                try {
                    System.out.println("Game ID: ");
                    gameID=scanner.nextInt();
                }
                catch(java.util.InputMismatchException e){
                    System.out.println("Input not accepted");
                    scanner.nextLine();
                    loggedInCommands(server);
                    break;
                }
                JoinGameRequest req1 = new JoinGameRequest(null, gameID);
                JoinGameResponse resp1 = server.joinGame(req1);
                if(resp1.getMessage() != null) {
                    System.out.println(resp1.getMessage());
                    loggedInCommands(server);
                    break;
                }
                else{
                    System.out.println("<Successfully Joined as Observer>");
                    ChessBoard board = new ChessBoard();
                    board.resetBoard();
                    PrintBoard.drawForPlayer1(board);
                    System.out.print("\n");
                    PrintBoard.drawForPlayer2(board);
                    break;
                }
            case "help":
                loggedInCommands(server);
                break;
            case "logout":
                LogoutResponse resp2 = server.logout();
                if(resp2.getMessage() != null){
                    System.out.println(resp2.getMessage());
                    break;
                }
                else{
                    System.out.println("<Successful Logout>");
                    System.out.print("\n");
                    listCommands();
                    break;
                }
            case "quit":
                System.out.println("<Exited the Program>");
                System.exit(0);
                break;
            default:
                System.out.println("<Command not accepted>");
                loggedInCommands(server);
                break;
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