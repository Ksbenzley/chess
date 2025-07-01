package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator extends PMCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> moves = new ArrayList<>();

        int[][] directions = {
                {0, 1}, {1, 0},
                {0, -1}, {-1, 0}
        };

        for (int[] dir : directions){
            int step = 1;
            while(true){
                int newRow = position.getRow() + dir[0] * step;
                int newCol = position.getColumn() + dir[1] * step;

                if (newRow > 8 || newRow < 1 || newCol > 8 || newCol < 1){
                    break;
                }

                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece destPiece = board.getPiece(newPos);
                ChessPiece myPiece = board.getPiece(position);

                if (destPiece == null){
                    moves.add(new ChessMove(position, newPos, null));
                }else if (destPiece.getTeamColor() != myPiece.getTeamColor()){
                    moves.add(new ChessMove(position, newPos, null));
                    break;
                }else{
                    break;
                }
                step++;
            }
        }
        return moves;
    }
}
