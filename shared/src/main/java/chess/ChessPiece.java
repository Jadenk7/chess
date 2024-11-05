package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type && Objects.equals(board, that.board) && Objects.equals(myPosition, that.myPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type, board, myPosition);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                ", board=" + board +
                ", myPosition=" + myPosition +
                '}';
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
        Collection<ChessMove> possibilities = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        int startRow = row;
        int startCol = col;
        while (row < 9 && col < 9) {
            if (row + 1 < 9 && col + 1 < 9) {
                ChessPosition aheadPosition = new ChessPosition(row + 1, col + 1);
                if (board.getPiece(aheadPosition) == null) {
                    possibilities.add(new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(row + 1, col + 1), null));
                    row += 1;
                    col += 1;
                    continue;
                } else {
                    ChessPiece myPiece = board.getPiece(position);
                    ChessPiece otherPiece = board.getPiece(aheadPosition);
                    if (myPiece.pieceColor != otherPiece.pieceColor) {
                        possibilities.add(new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(row + 1, col + 1), null));
                        break;
                    } else {
                        break;
                    }
                }
            }
            row += 1;
            col += 1;
        }
        row = startRow;
        col = startCol;
        while (row < 9 && 0 < col) {
            if (row + 1 < 9 && col - 1 > 0) {
                ChessPosition aheadPosition = new ChessPosition(row + 1, col - 1);
                if (board.getPiece(aheadPosition) == null) {
                    possibilities.add(new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(row + 1, col - 1), null));
                    row += 1;
                    col -= 1;
                    continue;
                } else {
                    ChessPiece myPiece = board.getPiece(position);
                    ChessPiece otherPiece = board.getPiece(aheadPosition);
                    if (myPiece.pieceColor != otherPiece.pieceColor) {
                        possibilities.add(new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(row + 1, col - 1), null));
                        break;
                    } else {
                        break;
                    }
                }
            }
            row += 1;
            col -= 1;
        }
        row = startRow;
        col = startCol;
        while (0 < row && 0 < col) {
            if (row - 1 > 0 && col - 1 > 0) {
                ChessPosition aheadPosition = new ChessPosition(row - 1, col - 1);
                if (board.getPiece(aheadPosition) == null) {
                    possibilities.add(new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(row - 1, col - 1), null));
                    row -= 1;
                    col -= 1;
                    continue;
                } else {
                    ChessPiece myPiece = board.getPiece(position);
                    ChessPiece otherPiece = board.getPiece(aheadPosition);
                    if (myPiece.pieceColor != otherPiece.pieceColor) {
                        possibilities.add(new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(row - 1, col - 1), null));
                        break;
                    } else {
                        break;
                    }
                }
            }
            row -= 1;
            col -= 1;
        }
        row = startRow;
        col = startCol;
        while (0 < row && col < 9) {
            if (row - 1 > 0 && col + 1 < 9) {
                ChessPosition aheadPosition = new ChessPosition(row - 1, col + 1);
                if (board.getPiece(aheadPosition) == null) {
                    possibilities.add(new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(row - 1, col + 1), null));
                    row -= 1;
                    col += 1;
                    continue;
                } else {
                    ChessPiece myBishop = board.getPiece(position);
                    ChessPiece bishopNeighbor = board.getPiece(aheadPosition);
                    if (myBishop.pieceColor != bishopNeighbor.pieceColor) {
                        possibilities.add(new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(row - 1, col + 1), null));
                        break;
                    } else {
                        break;
                    }
                }
            }
            row -= 1;
            col += 1;
        }
        return possibilities;
    }

    private Collection<ChessMove> kingCalc(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibilities = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        Collection<ChessPosition> positions = new ArrayList<>();
        if (col - 1 > 0) {
            positions.add(new ChessPosition(row, col - 1));
        }
        if (col + 1 < 9) {
            positions.add(new ChessPosition(row, col + 1));
        }
        if (row - 1 > 0 && col - 1 > 0) {
            positions.add(new ChessPosition(row - 1, col - 1));
        }
        if (row - 1 > 0) {
            positions.add(new ChessPosition(row - 1, col));
        }
        if (row - 1 > 0 && col + 1 < 9) {
            positions.add(new ChessPosition(row - 1, col + 1));
        }
        if (row + 1 < 9 && col - 1 > 0) {
            positions.add(new ChessPosition(row + 1, col - 1));
        }
        if (row + 1 < 9) {
            positions.add(new ChessPosition(row + 1, col));
        }
        if (row + 1 < 9 && col + 1 < 9) {
            positions.add(new ChessPosition(row + 1, col + 1));
        }
        for (ChessPosition pos : positions) {
            if (board.getPiece(pos) == null) {
                possibilities.add(new ChessMove(new ChessPosition(row, col), pos, null));
            } else {
                ChessPiece myKing = board.getPiece(position);
                ChessPiece kingNeighbor = board.getPiece(pos);
                if (myKing.pieceColor != kingNeighbor.pieceColor) {
                    possibilities.add(new ChessMove(new ChessPosition(row, col), pos, null));
                }
            }
        }
        return possibilities;
    }

    private Collection<ChessMove> knightCalc(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibilities = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        Collection<ChessPosition> positions = new ArrayList<>();
        if (row + 2 < 9 && col + 1 < 9) {
            ChessPosition aheadPosition1 = new ChessPosition(row + 2, col + 1);
            if (board.getPiece(aheadPosition1) == null) {
                possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition1, null));
            } else {
                ChessPiece myKnight = board.getPiece(position);
                ChessPiece knightNeighbor = board.getPiece(aheadPosition1);
                if (myKnight.pieceColor != knightNeighbor.pieceColor) {
                    possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition1, null));
                }
            }
        }
        if (row + 2 < 9 && col - 1 > 0) {
            ChessPosition aheadPosition2 = new ChessPosition(row + 2, col - 1);
            if (board.getPiece(aheadPosition2) == null) {
                possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition2, null));
            } else {
                ChessPiece myKnight = board.getPiece(position);
                ChessPiece knightNeighbor = board.getPiece(aheadPosition2);
                if (myKnight.pieceColor != knightNeighbor.pieceColor) {
                    possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition2, null));
                }
            }
        }
        if (row + 1 < 9 && col + 2 < 9) {
            ChessPosition aheadPosition3 = new ChessPosition(row + 1, col + 2);
            if (board.getPiece(aheadPosition3) == null) {
                possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
            } else {
                ChessPiece myKnight = board.getPiece(position);
                ChessPiece knightNeighbor = board.getPiece(aheadPosition3);
                if (myKnight.pieceColor != knightNeighbor.pieceColor) {
                    possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
                }
            }
        }
        if (row + 1 < 9 && col - 2 > 0) {
            ChessPosition aheadPosition3 = new ChessPosition(row + 1, col - 2);
            if (board.getPiece(aheadPosition3) == null) {
                possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
            } else {
                ChessPiece myKnight = board.getPiece(position);
                ChessPiece knightNeighbor = board.getPiece(aheadPosition3);
                if (myKnight.pieceColor != knightNeighbor.pieceColor) {
                    possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
                }
            }
        }
        if (row - 1 > 0 && col + 2 < 9) {
            ChessPosition aheadPosition3 = new ChessPosition(row - 1, col + 2);
            if (board.getPiece(aheadPosition3) == null) {
                possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
            } else {
                ChessPiece myKnight = board.getPiece(position);
                ChessPiece knightNeighbor = board.getPiece(aheadPosition3);
                if (myKnight.pieceColor != knightNeighbor.pieceColor) {
                    possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
                }
            }
        }
        if (row - 1 > 0 && col - 2 > 0) {
            ChessPosition aheadPosition3 = new ChessPosition(row - 1, col - 2);
            if (board.getPiece(aheadPosition3) == null) {
                possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
            } else {
                ChessPiece myKnight = board.getPiece(position);
                ChessPiece knightNeighbor = board.getPiece(aheadPosition3);
                if (myKnight.pieceColor != knightNeighbor.pieceColor) {
                    possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
                }
            }
        }
        if (row - 2 > 0 && col + 1 < 9) {
            ChessPosition aheadPosition3 = new ChessPosition(row - 2, col + 1);
            if (board.getPiece(aheadPosition3) == null) {
                possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
            } else {
                ChessPiece myKnight = board.getPiece(position);
                ChessPiece knightNeighbor = board.getPiece(aheadPosition3);
                if (myKnight.pieceColor != knightNeighbor.pieceColor) {
                    possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
                }
            }
        }
        if (row - 2 > 0 && col - 1 > 0) {
            ChessPosition aheadPosition3 = new ChessPosition(row - 2, col - 1);
            if (board.getPiece(aheadPosition3) == null) {
                possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
            } else {
                ChessPiece myKnight = board.getPiece(position);
                ChessPiece knightNeighbor = board.getPiece(aheadPosition3);
                if (myKnight.pieceColor != knightNeighbor.pieceColor) {
                    possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition3, null));
                }
            }
        }
        return possibilities;
    }

    private Collection<ChessMove> pawnCalc(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibilities = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        if (board.getPiece(position).pieceColor == ChessGame.TeamColor.WHITE) {
            if (row == 2) {
                ChessPosition aheadPosition = new ChessPosition(row + 1, col);
                if (col + 1 < 8) {
                    ChessPosition diagChecker1 = new ChessPosition(row + 1, col + 1);
                    if (board.getPiece(diagChecker1) != null) {
                        ChessPiece myPawn = board.getPiece(position);
                        ChessPiece pawnNeighbor = board.getPiece(diagChecker1);
                        if (myPawn.pieceColor != pawnNeighbor.pieceColor) {
                            if (row + 1 == 8) {
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, PieceType.ROOK));
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, PieceType.KNIGHT));
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, PieceType.BISHOP));
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, PieceType.QUEEN));
                            } else {
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, null));
                            }
                        }
                    }
                }
                if (col - 1 > 0) {
                    ChessPosition diagChecker2 = new ChessPosition(row + 1, col - 1);
                    if (board.getPiece(diagChecker2) != null) {
                        ChessPiece myPawn = board.getPiece(position);
                        ChessPiece pawnNeighbor = board.getPiece(diagChecker2);
                        if (myPawn.pieceColor != pawnNeighbor.pieceColor) {
                            if (row + 1 == 8) {
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, PieceType.ROOK));
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, PieceType.KNIGHT));
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, PieceType.BISHOP));
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, PieceType.QUEEN));
                            } else {
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, null));
                            }
                        }
                    }
                }
                if (board.getPiece(aheadPosition) == null) {
                    possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, null));
                    int row2 = position.getRow() + 1;
                    ChessPosition aheadPosition2 = new ChessPosition(row2 + 1, col);
                    if (board.getPiece(aheadPosition2) == null) {
                        possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition2, null));
                    }
                }
            } else {
                ChessPosition aheadPosition = new ChessPosition(row + 1, col);
                if (col + 1 < 8) {
                    ChessPosition diagChecker1 = new ChessPosition(row + 1, col + 1);
                    if (board.getPiece(diagChecker1) != null) {
                        ChessPiece myPawn = board.getPiece(position);
                        ChessPiece pawnNeighbor = board.getPiece(diagChecker1);
                        if (myPawn.pieceColor != pawnNeighbor.pieceColor) {
                            possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, null));
                        }
                    }
                }
                if (col - 1 > 0) {
                    ChessPosition diagChecker2 = new ChessPosition(row + 1, col - 1);
                    if (board.getPiece(diagChecker2) != null) {
                        ChessPiece myPawn = board.getPiece(position);
                        ChessPiece pawnNeighbor = board.getPiece(diagChecker2);
                        if (myPawn.pieceColor != pawnNeighbor.pieceColor) {
                            possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, null));
                        }
                    }
                }
                if (board.getPiece(aheadPosition) == null) {
                    if (row + 1 == 8) {
                        possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, PieceType.ROOK));
                        possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, PieceType.KNIGHT));
                        possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, PieceType.BISHOP));
                        possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, PieceType.QUEEN));
                    } else {
                        possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, null));
                    }
                }
            }
        } else {
            if (row == 7) {
                ChessPosition aheadPosition = new ChessPosition(row - 1, col);
                if (col + 1 < 8) {
                    ChessPosition diagChecker1 = new ChessPosition(row - 1, col + 1);
                    if (board.getPiece(diagChecker1) != null) {
                        ChessPiece myPawn = board.getPiece(position);
                        ChessPiece pawnNeighbor = board.getPiece(diagChecker1);
                        if (myPawn.pieceColor != pawnNeighbor.pieceColor) {
                            possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, null));
                        }
                    }
                }
                if (col - 1 > 0) {
                    ChessPosition diagChecker2 = new ChessPosition(row - 1, col - 1);
                    if (board.getPiece(diagChecker2) != null) {
                        ChessPiece myPawn = board.getPiece(position);
                        ChessPiece pawnNeighbor = board.getPiece(diagChecker2);
                        if (myPawn.pieceColor != pawnNeighbor.pieceColor) {
                            possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, null));
                        }
                    }
                }
                if (board.getPiece(aheadPosition) == null) {
                    possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, null));
                    int row2 = position.getRow() - 1;
                    ChessPosition aheadPosition2 = new ChessPosition(row2 - 1, col);
                    if (board.getPiece(aheadPosition2) == null) {
                        possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition2, null));
                    }
                }
            } else {
                ChessPosition aheadPosition = new ChessPosition(row - 1, col);
                if (col + 1 < 8) {
                    ChessPosition diagChecker1 = new ChessPosition(row - 1, col + 1);
                    if (board.getPiece(diagChecker1) != null) {
                        ChessPiece myPawn = board.getPiece(position);
                        ChessPiece pawnNeighbor = board.getPiece(diagChecker1);
                        if (myPawn.pieceColor != pawnNeighbor.pieceColor) {
                            if (row - 1 == 1) {
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, PieceType.ROOK));
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, PieceType.KNIGHT));
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, PieceType.BISHOP));
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, PieceType.QUEEN));
                            } else {
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker1, null));
                            }
                        }
                    }
                }
                if (col - 1 > 0) {
                    ChessPosition diagChecker2 = new ChessPosition(row - 1, col - 1);
                    if (board.getPiece(diagChecker2) != null) {
                        ChessPiece myPawn = board.getPiece(position);
                        ChessPiece pawnNeighbor = board.getPiece(diagChecker2);
                        if (myPawn.pieceColor != pawnNeighbor.pieceColor) {
                            if (row - 1 == 1) {
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, PieceType.ROOK));
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, PieceType.KNIGHT));
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, PieceType.BISHOP));
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, PieceType.QUEEN));
                            } else {
                                possibilities.add(new ChessMove(new ChessPosition(row, col), diagChecker2, null));
                            }
                        }
                    }
                }
                if (board.getPiece(aheadPosition) == null) {
                    if (row - 1 == 1) {
                        possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, PieceType.ROOK));
                        possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, PieceType.KNIGHT));
                        possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, PieceType.BISHOP));
                        possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, PieceType.QUEEN));
                    } else {
                        possibilities.add(new ChessMove(new ChessPosition(row, col), aheadPosition, null));
                    }
                }
            }
        }
        return possibilities;
    }

    private Collection<ChessMove> rookCalc(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> possibilities = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        int startingRow = position.getRow();
        int startingCol = position.getColumn();
        if (row != 8) {
            while (row < 8) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPosition aheadPosition = new ChessPosition(row + 1, col);
                if (row + 1 == 8 && board.getPiece(aheadPosition) == null) {
                    possibilities.add(new ChessMove(new ChessPosition(startingRow, startingCol), aheadPosition, null));
                    break;
                }
                if (board.getPiece(aheadPosition) != null) {
                    ChessPiece myRook = board.getPiece(position);
                    ChessPiece rookNeighbor = board.getPiece(aheadPosition);
                    if (myRook.pieceColor != rookNeighbor.pieceColor) {
                        possibilities.add(new ChessMove(new ChessPosition(startingRow, startingCol), aheadPosition, null));
                        break;
                    } else {
                        break;
                    }
                } else {
                    possibilities.add(new ChessMove(new ChessPosition(startingRow, startingCol), aheadPosition, null));
                    row += 1;
                }
            }
        }
        row = startingRow;
        if (row != 1) {
            while (row > 1) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPosition aheadPosition = new ChessPosition(row - 1, col);
                if (row - 1 == 1 && board.getPiece(aheadPosition) == null) {
                    possibilities.add(new ChessMove(new ChessPosition(startingRow, startingCol), aheadPosition, null));
                    break;
                }
                if (board.getPiece(aheadPosition) != null) {
                    ChessPiece myRook = board.getPiece(position);
                    ChessPiece rookNeighbor = board.getPiece(aheadPosition);
                    if (myRook.pieceColor != rookNeighbor.pieceColor) {
                        possibilities.add(new ChessMove(new ChessPosition(startingRow, startingCol), aheadPosition, null));
                        break;
                    } else {
                        break;
                    }
                } else {
                    possibilities.add(new ChessMove(new ChessPosition(startingRow, startingCol), aheadPosition, null));
                    row -= 1;
                }
            }
        }
        row = startingRow;
        if (col != 8) {
            while (col < 8) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPosition aheadPosition = new ChessPosition(row, col + 1);
                if (col + 1 == 8 && board.getPiece(aheadPosition) == null) {
                    possibilities.add(new ChessMove(new ChessPosition(startingRow, startingCol), aheadPosition, null));
                    break;
                }
                if (board.getPiece(aheadPosition) != null) {
                    ChessPiece myRook = board.getPiece(position);
                    ChessPiece rookNeighbor = board.getPiece(aheadPosition);
                    if (myRook.pieceColor != rookNeighbor.pieceColor) {
                        possibilities.add(new ChessMove(new ChessPosition(startingRow, startingCol), aheadPosition, null));
                        break;
                    } else {
                        break;
                    }
                } else {
                    possibilities.add(new ChessMove(new ChessPosition(startingRow, startingCol), aheadPosition, null));
                    col += 1;
                }
            }
        }
        col = startingCol;
        if (col != 1) {
            while (col > 1) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPosition aheadPosition = new ChessPosition(row, col - 1);
                if (col - 1 == 1 && board.getPiece(aheadPosition) == null) {
                    possibilities.add(new ChessMove(new ChessPosition(startingRow, startingCol), aheadPosition, null));
                    break;
                }
                if (board.getPiece(aheadPosition) != null) {
                    ChessPiece myRook = board.getPiece(position);
                    ChessPiece rookNeighbor = board.getPiece(aheadPosition);
                    if (myRook.pieceColor != rookNeighbor.pieceColor) {
                        possibilities.add(new ChessMove(new ChessPosition(startingRow, startingCol), aheadPosition, null));
                        break;
                    } else {
                        break;
                    }
                } else {
                    possibilities.add(new ChessMove(new ChessPosition(startingRow, startingCol), aheadPosition, null));
                    col -= 1;
                }
            }
        }
        return possibilities;
    }

    private Collection<ChessMove> queenCalc(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves1 = rookCalc(board, position);
        Collection<ChessMove> moves2 = bishopCalc(board, position);
        moves1.addAll(moves2);
        return moves1;
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
        if (this.type == PieceType.QUEEN) {
            return queenCalc(board, myPosition);
        }
        return null;
    }
}