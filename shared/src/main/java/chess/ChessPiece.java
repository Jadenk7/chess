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
    private Collection<ChessMove> bishopCalc(ChessBoard board, ChessPosition position) {
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
                } else {
                    ChessPiece myPiece = board.getPiece(position);
                    ChessPiece otherPiece = board.getPiece(aheadPosition);
                    if (myPiece.pieceColor != otherPiece.pieceColor) {
                        Possibilities.add(new ChessMove(new ChessPosition(start_row, start_col), new ChessPosition(row + 1, col + 1), null));
                        break;
                    } else {
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
                } else {
                    ChessPiece myPiece = board.getPiece(position);
                    ChessPiece otherPiece = board.getPiece(aheadPosition);
                    if (myPiece.pieceColor != otherPiece.pieceColor) {
                        Possibilities.add(new ChessMove(new ChessPosition(start_row, start_col), new ChessPosition(row + 1, col - 1), null));
                        break;
                    } else {
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
                } else {
                    ChessPiece myPiece = board.getPiece(position);
                    ChessPiece otherPiece = board.getPiece(aheadPosition);
                    if (myPiece.pieceColor != otherPiece.pieceColor) {
                        Possibilities.add(new ChessMove(new ChessPosition(start_row, start_col), new ChessPosition(row - 1, col - 1), null));
                        break;
                    } else {
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
                } else {
                    ChessPiece myBishop = board.getPiece(position);
                    ChessPiece bishopNeighbor = board.getPiece(aheadPosition);
                    if (myBishop.pieceColor != bishopNeighbor.pieceColor) {
                        Possibilities.add(new ChessMove(new ChessPosition(start_row, start_col), new ChessPosition(row - 1, col + 1), null));
                        break;
                    } else {
                        break;
                    }
                }
            }
            row -= 1;
            col += 1;
        }
        return Possibilities;
    }

    private Collection<ChessMove> kingCalc(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> Possibilities = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        Collection<ChessPosition> Positions = new ArrayList<>();
        if (col - 1 > 0) {
            Positions.add(new ChessPosition(row, col - 1));
        }
        if (col + 1 < 9) {
            Positions.add(new ChessPosition(row, col + 1));
        }
        if (row - 1 > 0 && col - 1 > 0) {
            Positions.add(new ChessPosition(row - 1, col - 1));
        }
        if (row - 1 > 0) {
            Positions.add(new ChessPosition(row - 1, col));
        }
        if (row - 1 > 0 && col + 1 < 9) {
            Positions.add(new ChessPosition(row - 1, col + 1));
        }
        if (row + 1 < 9 && col - 1 > 0) {
            Positions.add(new ChessPosition(row + 1, col - 1));
        }
        if (row + 1 < 9) {
            Positions.add(new ChessPosition(row + 1, col));
        }
        if (row + 1 < 9 && col + 1 < 9) {
            Positions.add(new ChessPosition(row + 1, col + 1));
        }
        for (ChessPosition pos : Positions) {
            if (board.getPiece(pos) == null) {
                Possibilities.add(new ChessMove(new ChessPosition(row, col), pos, null));
            } else {
                ChessPiece myKing = board.getPiece(position);
                ChessPiece kingNeighbor = board.getPiece(pos);
                if (myKing.pieceColor != kingNeighbor.pieceColor) {
                    Possibilities.add(new ChessMove(new ChessPosition(row, col), pos, null));
                }
            }
        }
        return Possibilities;
    }

    private Collection<ChessMove> knightCalc(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> Possibilities = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        Collection<ChessPosition> Positions = new ArrayList<>();
        if (row + 2 < 9 && col + 1 < 9) {
            ChessPosition aheadPosition1 = new ChessPosition(row + 2, col + 1);
            if (board.getPiece(aheadPosition1) == null) {
                Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition1, null));
            } else {
                ChessPiece myKnight = board.getPiece(position);
                ChessPiece knightNeighbor = board.getPiece(aheadPosition1);
                if (myKnight.pieceColor != knightNeighbor.pieceColor) {
                    Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition1, null));
                }
            }
        }
        if (row + 2 < 9 && col - 1 > 0) {
            ChessPosition aheadPosition2 = new ChessPosition(row + 2, col - 1);
            if (board.getPiece(aheadPosition2) == null) {
                Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition2, null));
            } else {
                ChessPiece myKnight = board.getPiece(position);
                ChessPiece knightNeighbor = board.getPiece(aheadPosition2);
                if (myKnight.pieceColor != knightNeighbor.pieceColor) {
                    Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition2, null));
                }
            }
        }
        if (row + 1 < 9 && col + 2 < 9) {
            ChessPosition aheadPosition3 = new ChessPosition(row + 1, col + 2);
            if (board.getPiece(aheadPosition3) == null) {
                Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
            } else {
                ChessPiece myKnight = board.getPiece(position);
                ChessPiece knightNeighbor = board.getPiece(aheadPosition3);
                if (myKnight.pieceColor != knightNeighbor.pieceColor) {
                    Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
                }
            }
        }
        if (row + 1 < 9 && col - 2 > 0) {
            ChessPosition aheadPosition3 = new ChessPosition(row + 1, col - 2);
            if (board.getPiece(aheadPosition3) == null) {
                Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
            } else {
                ChessPiece myKnight = board.getPiece(position);
                ChessPiece knightNeighbor = board.getPiece(aheadPosition3);
                if (myKnight.pieceColor != knightNeighbor.pieceColor) {
                    Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
                }
            }
        }
        if (row - 1 > 0 && col + 2 < 9) {
            ChessPosition aheadPosition3 = new ChessPosition(row - 1, col + 2);
            if (board.getPiece(aheadPosition3) == null) {
                Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
            } else {
                ChessPiece myKnight = board.getPiece(position);
                ChessPiece knightNeighbor = board.getPiece(aheadPosition3);
                if (myKnight.pieceColor != knightNeighbor.pieceColor) {
                    Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
                }
            }
        }
        if (row - 1 > 0 && col - 2 > 0) {
            ChessPosition aheadPosition3 = new ChessPosition(row - 1, col - 2);
            if (board.getPiece(aheadPosition3) == null) {
                Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
            } else {
                ChessPiece myKnight = board.getPiece(position);
                ChessPiece knightNeighbor = board.getPiece(aheadPosition3);
                if (myKnight.pieceColor != knightNeighbor.pieceColor) {
                    Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
                }
            }
        }
        if (row - 2 > 0 && col + 1 < 9) {
            ChessPosition aheadPosition3 = new ChessPosition(row - 2, col + 1);
            if (board.getPiece(aheadPosition3) == null) {
                Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
            } else {
                ChessPiece myKnight = board.getPiece(position);
                ChessPiece knightNeighbor = board.getPiece(aheadPosition3);
                if (myKnight.pieceColor != knightNeighbor.pieceColor) {
                    Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
                }
            }
        }
        if (row - 2 > 0 && col - 1 > 0) {
            ChessPosition aheadPosition3 = new ChessPosition(row - 2, col - 1);
            if (board.getPiece(aheadPosition3) == null) {
                Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
            } else {
                ChessPiece myKnight = board.getPiece(position);
                ChessPiece knightNeighbor = board.getPiece(aheadPosition3);
                if (myKnight.pieceColor != knightNeighbor.pieceColor) {
                    Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
                }
            }
        }
        return Possibilities;
    }
    private Collection<ChessMove> pawnCalc(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> Possibilities = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        if (board.getPiece(position).pieceColor == ChessGame.TeamColor.WHITE){
            if (row == 2) {
                ChessPosition aheadPosition = new ChessPosition(row + 1, col);
                ChessPosition diagChecker1 = new ChessPosition(row + 1, col + 1);
                ChessPosition diagChecker2 = new ChessPosition(row + 1, col - 1);
                if (board.getPiece(aheadPosition) == null) {
                    Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, null));
                }
                if (board.getPiece(diagChecker1) != null) {
                    ChessPiece myPawn = board.getPiece(position);
                    ChessPiece pawnNeighbor = board.getPiece(diagChecker1);
                    if (myPawn.pieceColor != pawnNeighbor.pieceColor) {
                        Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, null));
                    }
                }
                if (board.getPiece(diagChecker2) != null) {
                    ChessPiece myPawn = board.getPiece(position);
                    ChessPiece pawnNeighbor = board.getPiece(diagChecker2);
                    if (myPawn.pieceColor != pawnNeighbor.pieceColor) {
                        Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, null));
                    }
                }
                if (board.getPiece(aheadPosition) == null) {
                    int row2 = position.getRow() + 1;
                    ChessPosition aheadPosition2 = new ChessPosition(row2 + 1, col);
                    if (board.getPiece(aheadPosition2) == null) {
                        Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition2, null));
                    }
                }
            }
            else {
                ChessPosition aheadPosition = new ChessPosition(row + 1, col);
                ChessPosition diagChecker1 = new ChessPosition(row + 1, col + 1);
                ChessPosition diagChecker2 = new ChessPosition(row + 1, col - 1);
                if (board.getPiece(aheadPosition) == null) {
                    if (row + 1 == 8){
                        Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, PieceType.ROOK));
                        Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, PieceType.KNIGHT));
                        Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, PieceType.BISHOP));
                        Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, PieceType.QUEEN));
                    }
                    else {
                        Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, null));
                    }
                }
                if (board.getPiece(diagChecker1) != null) {
                    ChessPiece myPawn = board.getPiece(position);
                    ChessPiece pawnNeighbor = board.getPiece(diagChecker1);
                    if (myPawn.pieceColor != pawnNeighbor.pieceColor) {
                        if (row + 1 == 8){
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, PieceType.ROOK));
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, PieceType.KNIGHT));
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, PieceType.BISHOP));
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, PieceType.QUEEN));
                        }
                        else {
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, null));
                        }
                    }
                }
                if (board.getPiece(diagChecker2) != null) {
                    ChessPiece myPawn = board.getPiece(position);
                    ChessPiece pawnNeighbor = board.getPiece(diagChecker2);
                    if (myPawn.pieceColor != pawnNeighbor.pieceColor) {
                        if (row + 1 == 8) {
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, PieceType.ROOK));
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, PieceType.KNIGHT));
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, PieceType.BISHOP));
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, PieceType.QUEEN));
                        }
                        else {
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, null));
                        }
                    }
                }
            }
        }
        else {
            if (row == 7) {
                ChessPosition aheadPosition = new ChessPosition(row - 1, col);
                ChessPosition diagChecker1 = new ChessPosition(row - 1, col + 1);
                ChessPosition diagChecker2 = new ChessPosition(row - 1, col - 1);
                if (board.getPiece(aheadPosition) == null) {
                    Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, null));
                }
                if (board.getPiece(diagChecker1) != null) {
                    ChessPiece myPawn = board.getPiece(position);
                    ChessPiece pawnNeighbor = board.getPiece(diagChecker1);
                    if (myPawn.pieceColor != pawnNeighbor.pieceColor) {
                        Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, null));
                    }
                }
                if (board.getPiece(diagChecker2) != null) {
                    ChessPiece myPawn = board.getPiece(position);
                    ChessPiece pawnNeighbor = board.getPiece(diagChecker2);
                    if (myPawn.pieceColor != pawnNeighbor.pieceColor) {
                        Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, null));
                    }
                }
                if (board.getPiece(aheadPosition) == null) {
                    int row2 = position.getRow() - 1;
                    ChessPosition aheadPosition2 = new ChessPosition(row2 - 1, col);
                    if (board.getPiece(aheadPosition2) == null) {
                        Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition2, null));
                    }
                }
            }
            else {
                ChessPosition aheadPosition = new ChessPosition(row - 1, col);
                ChessPosition diagChecker1 = new ChessPosition(row - 1, col + 1);
                ChessPosition diagChecker2 = new ChessPosition(row - 1, col - 1);
                if (board.getPiece(aheadPosition) == null) {
                    if (row - 1 == 1){
                        Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, PieceType.ROOK));
                        Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, PieceType.KNIGHT));
                        Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, PieceType.BISHOP));
                        Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, PieceType.QUEEN));
                    }
                    else {
                        Possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, null));
                    }
                }
                if (board.getPiece(diagChecker1) != null) {
                    ChessPiece myPawn = board.getPiece(position);
                    ChessPiece pawnNeighbor = board.getPiece(diagChecker1);
                    if (myPawn.pieceColor != pawnNeighbor.pieceColor) {
                        if (row - 1 == 1){
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, PieceType.ROOK));
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, PieceType.KNIGHT));
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, PieceType.BISHOP));
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, PieceType.QUEEN));
                        }
                        else {
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, null));
                        }
                    }
                }
                if (board.getPiece(diagChecker2) != null) {
                    ChessPiece myPawn = board.getPiece(position);
                    ChessPiece pawnNeighbor = board.getPiece(diagChecker2);
                    if (myPawn.pieceColor != pawnNeighbor.pieceColor) {
                        if (row - 1 == 1) {
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, PieceType.ROOK));
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, PieceType.KNIGHT));
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, PieceType.BISHOP));
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, PieceType.QUEEN));
                        }
                        else {
                            Possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, null));
                        }
                    }
                }
            }
        }
        return Possibilities;
    }

    private Collection<ChessMove> rookCalc(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> Possibilities = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        return Possibilities;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (this.type == PieceType.BISHOP) {
            return bishopCalc(board, myPosition);
        }
        if (this.type == PieceType.KING) {
            return kingCalc(board, myPosition);
        }
        if (this.type == PieceType.KNIGHT) {
            return knightCalc(board, myPosition);
        }
        if (this.type == PieceType.PAWN) {
            return pawnCalc(board, myPosition);
        }
        if (this.type == PieceType.ROOK) {
            return rookCalc(board, myPosition);
        }
        return null;
    }
}