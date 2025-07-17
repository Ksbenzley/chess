package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends PMCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int[][] directions = {
                {-1, 1}, {0, 1}, {1, 1},
                {-1, 0},          {1, 0},
                {-1, -1}, {0, -1}, {1, -1}
        };
        return getLinearMoves(board, position, directions);
    }
}
