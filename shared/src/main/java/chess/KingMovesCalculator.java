package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KingMovesCalculator extends PMCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> moves = new ArrayList<>();

        int[][] directions = {
                {-1, 1}, {0, 1}, {1, 1},
                {-1, 0}, {1, 0},
                {-1, -1}, {0, -1}, {1, -1}
        };

        for (int[] dir : directions){
            int newRow = position.getRow() + dir[0];
            int newCol = position.getColumn() + dir[1];

            if (newRow <= 7 && newRow >= 0 && newCol <= 7 && newCol >= 0){
                ChessPosition x = new ChessPosition(newRow, newCol);
                moves.add(new ChessMove(position, x, null));
            }
        }
        return moves;
    }
}
