package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;
    private ChessBoard board;
    private ChessPosition myPosition;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * @return Which team this chess piece belongs to
     */

    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> Bishop_Calc(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> Possibilities = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        int start_row = row;
        int start_col = col;
        while (row < 9 && col < 9) {
            if (row + 1 < 9 && col + 1 < 9) {
                ChessPosition aheadPosition = new ChessPosition(row + 1, col + 1);
                if (board.getPiece(aheadPosition) == null) {
                    Possibilities.add(new ChessMove(new ChessPosition(start_row, start_col), new ChessPosition(row + 1, col + 1), null));
                    row += 1;
                    col += 1;
                    continue;
                }
                else {
                    ChessPiece myPiece = board.getPiece(position);
                    ChessPiece otherPiece = board.getPiece(aheadPosition);
                    if (myPiece.pieceColor != otherPiece.pieceColor) {
                        Possibilities.add(new ChessMove(new ChessPosition(start_row, start_col), new ChessPosition(row + 1, col + 1), null));
                        break;
                    }
                    else {
                        break;
                    }
                }
            }
            row += 1;
            col += 1;
        }
        row = start_row;
        col = start_col;
        while (row < 9 && 0 < col) {
            if (row + 1 < 9 && col - 1 > 0) {
                ChessPosition aheadPosition = new ChessPosition(row + 1, col - 1);
                if (board.getPiece(aheadPosition) == null) {
                    Possibilities.add(new ChessMove(new ChessPosition(start_row, start_col), new ChessPosition(row + 1, col - 1), null));
                    row += 1;
                    col -= 1;
                    continue;
                }
                else {
                    ChessPiece myPiece = board.getPiece(position);
                    ChessPiece otherPiece = board.getPiece(aheadPosition);
                    if (myPiece.pieceColor != otherPiece.pieceColor) {
                        Possibilities.add(new ChessMove(new ChessPosition(start_row, start_col), new ChessPosition(row + 1, col - 1), null));
                        break;
                    }
                    else {
                        break;
                    }
                }
            }
            row += 1;
            col -= 1;
        }
        row = start_row;
        col = start_col;
        while (0 < row && 0 < col) {
            if (row - 1 > 0 && col - 1 > 0) {
                ChessPosition aheadPosition = new ChessPosition(row - 1, col - 1);
                if (board.getPiece(aheadPosition) == null) {
                    Possibilities.add(new ChessMove(new ChessPosition(start_row, start_col), new ChessPosition(row - 1, col - 1), null));
                    row -= 1;
                    col -= 1;
                    continue;
                }
                else {
                    ChessPiece myPiece = board.getPiece(position);
                    ChessPiece otherPiece = board.getPiece(aheadPosition);
                    if (myPiece.pieceColor != otherPiece.pieceColor) {
                        Possibilities.add(new ChessMove(new ChessPosition(start_row, start_col), new ChessPosition(row - 1, col - 1), null));
                        break;
                    }
                    else {
                        break;
                    }
                }
            }
            row -= 1;
            col -= 1;
        }
        row = start_row;
        col = start_col;
        while (0 < row && col < 9) {
            if (row - 1 > 0 && col + 1 < 9) {
                ChessPosition aheadPosition = new ChessPosition(row - 1, col + 1);
                if (board.getPiece(aheadPosition) == null) {
                    Possibilities.add(new ChessMove(new ChessPosition(start_row, start_col), new ChessPosition(row - 1, col + 1), null));
                    row -= 1;
                    col += 1;
                    continue;
                }
                else {
                    ChessPiece myPiece = board.getPiece(position);
                    ChessPiece otherPiece = board.getPiece(aheadPosition);
                    if (myPiece.pieceColor != otherPiece.pieceColor) {
                        Possibilities.add(new ChessMove(new ChessPosition(start_row, start_col), new ChessPosition(row - 1, col + 1), null));
                        break;
                    }
                    else {
                        break;
                    }
                }
            }
            row -= 1;
            col += 1;
        }
        return Possibilities;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (this.type == PieceType.BISHOP) {
            return Bishop_Calc(board, myPosition);
        }
        return null;
    }
}