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

            if (newRow <= 8 && newRow >= 1 && newCol <= 8 && newCol >= 1){
                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece destinationPiece = board.getPiece(newPos);
                ChessPiece myPiece = board.getPiece(position);

                if (destinationPiece == null || myPiece.getTeamColor() != destinationPiece.getTeamColor()){
                    moves.add(new ChessMove(position, newPos, null));
                }

            }
        }
        return moves;
    }
}
