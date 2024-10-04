package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard checkers;
    private TeamColor teamSide = TeamColor.WHITE;

    public ChessGame() {
        this.checkers = new ChessBoard();
        checkers.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamSide;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamSide = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public void moveIt(ChessBoard board, ChessMove move) {
        ChessPiece startPiece = board.getPiece(move.getStartPosition());
        if (move.getPromotionPiece() != null) {
            board.addPiece(move.getStartPosition(), null);
            board.addPiece(move.getEndPosition(), new ChessPiece(getTeamTurn(), move.getPromotionPiece()));
        } else {
            board.addPiece(move.getStartPosition(), null);
            board.addPiece(move.getEndPosition(), startPiece);
        }
    }

    public void unmoveIt(ChessBoard board, ChessMove move, ChessPiece origStart, ChessPiece origEnd) {
        board.addPiece(move.getStartPosition(), origStart);
        board.addPiece(move.getEndPosition(), origEnd);
    }

    public ChessPosition kingFinder(TeamColor color) {
        ChessPosition myKing = null;
        for (int row = 1; row < 9; row++) {
            for (int column = 1; column < 9; column++) {
                ChessPiece currentPiece = checkers.getPiece(new ChessPosition(row, column));
                if (currentPiece != null) {
                    if (currentPiece.getTeamColor() == color) {
                        if (currentPiece.getPieceType() == ChessPiece.PieceType.KING) {
                            myKing = new ChessPosition(row, column);
                            break;
                        }
                    }
                }
            }
        }
        return myKing;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece myPiece = checkers.getPiece(startPosition);
        Collection<ChessMove> moveList = new ArrayList<>();
        for (ChessMove play : myPiece.pieceMoves(checkers, startPosition)) {
            ChessPiece originalStart = checkers.getPiece(play.getStartPosition());
            ChessPiece originalEnd = checkers.getPiece(play.getEndPosition());
            moveIt(checkers, play);
            if (isInCheck(myPiece.getTeamColor()) == false) {
                moveList.add(play);
            }
            unmoveIt(checkers, play, originalStart, originalEnd);
        }
        return moveList;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece startPiece = checkers.getPiece(move.getStartPosition());
        if (startPiece == null) {
            throw new InvalidMoveException("Not a piece! Why are you trying to move nothing, silly?");
        }
        Collection<ChessMove> movesAllowed = validMoves(move.getStartPosition());
        if (startPiece.getTeamColor() != this.getTeamTurn()) {
            throw new InvalidMoveException("Not your team! How kind of you to consider the needs of your opponent :)");
        }
        if (startPiece == null) {
            throw new InvalidMoveException("Not a piece! Why are you trying to move nothing, silly?");
        }
        if (movesAllowed.contains(move)) {
            moveIt(checkers, move);
            if (this.getTeamTurn() == TeamColor.WHITE) {
                this.setTeamTurn(TeamColor.BLACK);
            } else {
                this.setTeamTurn(TeamColor.WHITE);
            }
        } else {
            throw new InvalidMoveException("Illegal! Prohibited in the name of the law!");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        if (kingFinder(teamColor) == null) {
            return false;
        }
        for (int row = 1; row < 9; row++) {
            for (int column = 1; column < 9; column++) {
                ChessPosition currentPos = new ChessPosition(row, column);
                ChessPiece currentPiece = checkers.getPiece(currentPos);
                if (currentPiece != null) {
                    if (currentPiece.getTeamColor() != teamColor) {
                        for (ChessMove move : currentPiece.pieceMoves(checkers, currentPos)) {
                            if (move.getEndPosition().equals(kingFinder(teamColor))) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (kingFinder(teamColor) == null) {
            return false;
        }
        if (isInCheck(teamColor) == false) {
            return false;
        }
        for (int row = 1; row < 9; row++) {
            for (int column = 1; column < 9; column++) {
                ChessPosition currentPosition = new ChessPosition(row, column);
                ChessPiece currentPiece = checkers.getPiece(currentPosition);
                if (currentPiece != null) {
                    if (currentPiece.getTeamColor() == teamColor) {
                        for (ChessMove move : currentPiece.pieceMoves(checkers, currentPosition)) {
                            ChessPiece originalStart = checkers.getPiece(move.getStartPosition());
                            ChessPiece originalEnd = checkers.getPiece(move.getEndPosition());
                            moveIt(checkers, move);
                            if (isInCheck(teamColor) == false) {
                                unmoveIt(checkers, move, originalStart, originalEnd);
                                return false;
                            }
                            unmoveIt(checkers, move, originalStart, originalEnd);
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        for (int row = 1; row < 9; row++) {
            for (int column = 1; column < 9; column++) {
                ChessPosition currentPosition = new ChessPosition(row, column);
                ChessPiece currentPiece = checkers.getPiece(currentPosition);
                if (currentPiece != null) {
                    if (currentPiece.getTeamColor() == teamColor) {
                        Collection<ChessMove> movesAllowed = validMoves(currentPosition);
                        if (isInCheck(teamColor) == false) {
                            if (movesAllowed.isEmpty() == false) {
                                return false;

                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.checkers = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return checkers;
    }
}
