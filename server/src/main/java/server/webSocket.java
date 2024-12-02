package server;

import chess.*;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import serverMessages.*;
import userGameCommands.*;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;
import java.io.IOException;
import java.util.Set;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;


@WebSocket
public class webSocket {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand gameCom = new Gson().fromJson(message, UserGameCommand.class);
        connectCommand connectCom = new Gson().fromJson(message, connectCommand.class);
        makeMoveCommand moveCom = new Gson().fromJson(message, makeMoveCommand.class);
        leaveCommand leaveCom = new Gson().fromJson(message, leaveCommand.class);
        resignCommand resignCom = new Gson().fromJson(message, resignCommand.class);
        try {
            switch (gameCom.getCommandType()) {
                case CONNECT -> connectHandler(connectCom, session);
                case MAKE_MOVE -> makeMoveHandler(moveCom, session);
                case LEAVE -> leaveHandler(leaveCom, session);
                case RESIGN -> resignHandler(resignCom, session);
            }
        }
        catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }
    private void gameLoader(Session session, GameData game) throws IOException {
        Gson gson = new Gson();
        ServerMessage loadGame = new loadGameMessage(game);
        String jsonMessage = gson.toJson(loadGame);
        session.getRemote().sendString(jsonMessage);
    }

    private void connectHandler(connectCommand command, Session session) throws DataAccessException, IOException {
        AuthDAO auth = new AuthDAO();
        AuthData token = auth.returnToken(command.getAuthToken());
        if(token == null){
            session.getRemote().sendString(new Gson().toJson(new errorMessage("Error!")));
            return;
        }
        GameDAO dao = new GameDAO();
        String username = token.getName();
        int gameID = command.getGameID();
        GameData game = dao.returnGame(gameID);
        ChessGame.TeamColor color =  command.getColor();
        if(game == null){
            session.getRemote().sendString(new Gson().toJson(new errorMessage("Error!")));
            return;
        }
        if((color == ChessGame.TeamColor.WHITE && (game.getWhiteUsername() == null || !game.getWhiteUsername().equals(username)) || color == ChessGame.TeamColor.BLACK && (game.getBlackUsername() == null || !game.getBlackUsername().equals(username)))){
            session.getRemote().sendString(new Gson().toJson(new errorMessage("Error!")));
            return;
        }
        connections.add(gameID, command.getAuthToken(), session);
        gameLoader(session, game);
        Set<Connection> connectionList = connections.connectionList.get(gameID);
        for(Connection conn: connectionList){
            if(conn.getUsername().equals(command.getAuthToken()) == false){
                conn.getSession().getRemote().sendString(new Gson().toJson(new notificationMessage(username + " joined the " + color + " side")));
            }
        }
    }
    private void makeMoveHandler(makeMoveCommand command, Session session) throws DataAccessException, InvalidMoveException, IOException {
        AuthDAO auth = new AuthDAO();
        AuthData token = auth.returnToken(command.getAuthToken());
        String username = token.getName();
        GameDAO dao = new GameDAO();
        int gameID = command.getGameID();
        GameData game = dao.returnGame(gameID);
        ChessGame chessGame = game.getChessGame();
        if(chessGame.isInStalemate(chessGame.getTeamTurn()) || chessGame.isInCheckmate(chessGame.getTeamTurn()) || chessGame.gameOver()){
            session.getRemote().sendString(new Gson().toJson(new errorMessage("Error!")));
            return;
        }
        System.out.println(command.getMove().toString());
        System.out.println(chessGame.validMoves(command.getMove().getStartPosition()));
        if (chessGame.validMoves(command.getMove().getStartPosition()).contains(command.getMove())) {
            if (chessGame.getTeamTurn() == ChessGame.TeamColor.WHITE && game.getWhiteUsername().equals(username)) {
                chessGame.makeMove(command.getMove());
                game.setGame(chessGame);
                dao.updateGame(game);
            }
            else if (chessGame.getTeamTurn() == ChessGame.TeamColor.BLACK && game.getBlackUsername().equals(username)) {
                chessGame.makeMove(command.getMove());
                game.setGame(chessGame);
                dao.updateGame(game);
            }
            else {
                session.getRemote().sendString(new Gson().toJson(new errorMessage("Not your turn yet")));
                return;
            }
        }
        else {
            session.getRemote().sendString(new Gson().toJson(new errorMessage("Not a valid move")));
            return;
        }

        Set<Connection> connectionList = connections.connectionList.get(gameID);
        for (Connection conn : connectionList) {
            gameLoader(conn.getSession(), game);
            if (conn.getUsername().equals(command.getAuthToken()) == false) {
                conn.getSession().getRemote().sendString(new Gson().toJson(new notificationMessage(username + " made their move")));
            }
        }
    }
    private void leaveHandler(leaveCommand command, Session session) throws DataAccessException, IOException {
        AuthDAO auth = new AuthDAO();
        AuthData token = auth.returnToken(command.getAuthToken());
        String username = token.getName();
        int gameID = command.getGameID();
        GameDAO dao = new GameDAO();
        GameData game = dao.returnGame(gameID);
        if (game != null) {
            if (game.getWhiteUsername().equals(username) && game.getWhiteUsername() != null) {
                game.setWhiteUsername(null);
            }
            else if (game.getBlackUsername().equals(username) && game.getBlackUsername() != null) {
                game.setBlackUsername(null);
            }
            dao.updateGame(game);
            connections.remove(token.getAuth());
            Set<Connection> connectionList = connections.connectionList.get(gameID);
            for(Connection conn: connectionList){
                if(conn.getUsername().equals(command.getAuthToken()) == false){
                    conn.getSession().getRemote().sendString(new Gson().toJson(new notificationMessage(username + " left the game")));
                }
            }
            session.close();
        }
    }
    private void resignHandler(resignCommand command, Session session) throws DataAccessException, IOException {
        AuthDAO auth = new AuthDAO();
        AuthData token = auth.returnToken(command.getAuthToken());
        String username = token.getName();
        int gameID = command.getGameID();
        GameDAO dao = new GameDAO();
        GameData game = dao.returnGame(gameID);
        if (game.getChessGame().gameOver()) {
            session.getRemote().sendString(new Gson().toJson(new errorMessage("Error!")));
            return;
        }
        if (game.getWhiteUsername().equals(username) == false && game.getBlackUsername().equals(username) == false) {
            session.getRemote().sendString(new Gson().toJson(new errorMessage("Error!")));
            return;
        }
        else {
            game.getChessGame().resign();
            dao.updateGame(game);
            connections.remove(username);
            Set<Connection> connectionList=connections.connectionList.get(gameID);
            for (Connection conn : connectionList) {
                conn.getSession().getRemote().sendString(new Gson().toJson(new notificationMessage(username + " resigned")));
            }
            if (session.isOpen()) {
                session.close();
            }
        }
    }
}

