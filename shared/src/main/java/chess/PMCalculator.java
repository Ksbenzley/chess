package chess;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PMCalculator {
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

    protected Collection<ChessMove> getLinearMoves(ChessBoard board, ChessPosition position, int[][] directions) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece myPiece = board.getPiece(position);

        for (int[] dir : directions) {
            int step = 1;
            while (true) {
                int newRow = position.getRow() + dir[0] * step;
                int newCol = position.getColumn() + dir[1] * step;

                if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                    break;
                }

                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece destPiece = board.getPiece(newPos);

                if (destPiece == null) {
                    moves.add(new ChessMove(position, newPos, null));
                } else if (destPiece.getTeamColor() != myPiece.getTeamColor()) {
                    moves.add(new ChessMove(position, newPos, null));
                    break;
                } else {
                    break;
                }

                step++;
            }
        }

        return moves;
    }

}
