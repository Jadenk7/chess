package userGameCommands;
import chess.ChessMove;
import websocket.commands.UserGameCommand;
public class makeMoveCommand extends UserGameCommand {
    private ChessMove move;
    public makeMoveCommand(String authToken, Integer gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = (ChessMove) move;
    }
    public void setMove(ChessMove move) {
        this.move = (ChessMove) move;
    }
    public ChessMove getMove() {
        return move;
    }
}