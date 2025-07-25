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
    private final ChessPiece.PieceType type;

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

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PMCalculator calculator;

        switch (this.getPieceType()){
            case KING:
                calculator = new KingMovesCalculator();
                break;
            case BISHOP:
                calculator = new BishopMovesCalculator();
                break;
            case QUEEN:
                calculator = new QueenMovesCalculator();
                break;
            case ROOK:
                calculator = new RookMovesCalculator();
                break;
            case KNIGHT:
                calculator = new KnightMovesCalculator();
                break;
            case PAWN:
                calculator = new PawnMovesCalculator();
                break;
            default:
                return new ArrayList<>();
        }

        return calculator.pieceMoves(board, myPosition);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessPiece that = (ChessPiece) o;
        return Objects.equals(pieceColor, that.pieceColor) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode(){
        return Objects.hash(pieceColor, type);
    }
}
