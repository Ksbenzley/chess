package chess;

import java.util.Collection;
import java.util.ArrayList;

public class BishopMovesCalculator extends PMCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        int[][] directions = {
                {1, -1}, {-1, -1},
                {-1, 1}, {1, 1}
        };
        return getLinearMoves(board, position, directions);
    }
}
