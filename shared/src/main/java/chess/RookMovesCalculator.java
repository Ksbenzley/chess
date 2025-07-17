package chess;

import java.util.Collection;

public class RookMovesCalculator extends PMCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int[][] directions = {
                {0, 1}, {1, 0},
                {0, -1}, {-1, 0}
        };
        return getLinearMoves(board, position, directions);
    }
}
