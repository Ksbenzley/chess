package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (position.getRow() < 1 || position.getRow() > 8 || position.getColumn() < 1 || position.getColumn() > 8){
            throw new IllegalArgumentException("Invalid board position: (" + position.getRow() + ", " + position.getColumn() + ")");
        }
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int row = 1; row <= 8; row++){
            ChessPosition position = new ChessPosition(row, 1);
            if(position.getRow() == 2){
                for (int col = 1; col <= 8; col++){
                    addPiece(new ChessPosition(position.getRow(), col), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
                    //squares[row][col] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
                }
            }else if (position.getRow() == 7){
                for (int col = 1; col <= 8; col++){
                    addPiece(new ChessPosition(position.getRow(), col), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
                }
            }else if (position.getRow() == 1){
                for (int col = 1; col <= 8; col++){
                    switch (col){
                        case 1, 8:
                            addPiece(new ChessPosition(position.getRow(), col), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
                            break;
                        case 2, 7:
                            addPiece(new ChessPosition(position.getRow(), col), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
                            break;
                        case 3, 6:
                            addPiece(new ChessPosition(position.getRow(), col), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
                            break;
                        case 4:
                            addPiece(new ChessPosition(position.getRow(), col), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
                            break;
                        case 5:
                            addPiece(new ChessPosition(position.getRow(), col), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
                            break;
                    }
                }
            }else if (position.getRow() == 8){
                for (int col = 1; col <= 8; col++){
                    switch (col){
                        case 1, 8:
                            addPiece(new ChessPosition(position.getRow(), col), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
                            break;
                        case 2, 7:
                            addPiece(new ChessPosition(position.getRow(), col), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
                            break;
                        case 3, 6:
                            addPiece(new ChessPosition(position.getRow(), col), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
                            break;
                        case 4:
                            addPiece(new ChessPosition(position.getRow(), col), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
                            break;
                        case 5:
                            addPiece(new ChessPosition(position.getRow(), col), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
                            break;
                    }
                }
            }else if (position.getRow() == 3 || position.getRow() == 4 || position.getRow() == 5 || position.getRow() == 6){
                for (int col = 1; col <= 8; col++){
                    addPiece(new ChessPosition(position.getRow(), col), null);
                }
            }

        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}
