package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends PMCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> moves = new ArrayList<>();

        int[][] directions = {
                {-1, 2}, {1, 2},
                {2, 1}, {2, -1},
                {1, -2}, {-1, -2},
                {-2, -1}, {-2, 1}
        };

        for (int[] dir : directions){
            int newRow = position.getRow() + dir[0];
            int newCol = position.getColumn() + dir[1];

            if (newRow <= 8 && newRow >= 1 && newCol <= 8 && newCol >= 1) {
                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece destPiece = board.getPiece(newPos);
                ChessPiece myPiece = board.getPiece(position);

                if (destPiece == null || destPiece.getTeamColor() != myPiece.getTeamColor()) {
                    moves.add(new ChessMove(position, newPos, null));
                }
            }

        }



        return moves;
    }
}
