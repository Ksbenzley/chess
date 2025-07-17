package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PMCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> moves = new ArrayList<>();

        ChessPiece myPiece = board.getPiece(position);
        ChessGame.TeamColor color = myPiece.getTeamColor();

        int angle;
        if (color == ChessGame.TeamColor.WHITE){
            angle = 1;
        }else{
            angle = -1;
        }

        int row = position.getRow();
        int col = position.getColumn();

        //move one forward
        ChessPosition oneForward = new ChessPosition(row + angle, col);
        if(inBounds(oneForward) && board.getPiece(oneForward) == null){
            if (isPromotionRow(oneForward.getRow(), color)){
                for (ChessPiece.PieceType promo : promotionPieces()){
                    moves.add(new ChessMove(position, oneForward, promo));
                }
            }else{
                moves.add(new ChessMove(position, oneForward, null));
            }

            //move two if unmoved
            int startRow;
            if (color == ChessGame.TeamColor.WHITE){
                startRow = 2;
            }else{
                startRow = 7;
            }

            if (row == startRow){
                ChessPosition twoForward = new ChessPosition(row + angle * 2, col);
                if(inBounds(twoForward) && board.getPiece(twoForward) == null){
                    denestingHelper(twoForward, position, moves, color);
                }
            }
        }

        //diagonal capture
        int[] dCols = {-1, 1};
        for (int dc : dCols) {
            ChessPosition diag = new ChessPosition(row + angle, col + dc);
            if (inBounds(diag)) {
                ChessPiece target = board.getPiece(diag);
                if ((target != null && target.getTeamColor() != color)) {
                    denestingHelper(diag, position, moves, color);
                }
            }
        }
        return moves;
    }

    private void denestingHelper(ChessPosition twoForward, ChessPosition position, Collection<ChessMove> moves, ChessGame.TeamColor color){
        if (isPromotionRow(twoForward.getRow(), color)){
            for (ChessPiece.PieceType promo : promotionPieces()){
                moves.add(new ChessMove(position, twoForward, promo));
            }
        }else{
            moves.add(new ChessMove(position, twoForward, null));
        }
    }

    private boolean inBounds(ChessPosition pos){
        int row = pos.getRow();
        int col = pos.getColumn();
        if(row >= 1 && row <= 8 && col >= 1 && col <= 8){
            return true;
        }else{
            return false;
        }
    }

    private boolean isPromotionRow(int row, ChessGame.TeamColor color) {
        return (color == ChessGame.TeamColor.WHITE && row == 8) ||
                (color == ChessGame.TeamColor.BLACK && row == 1);
    }

    private ChessPiece.PieceType[] promotionPieces() {
        return new ChessPiece.PieceType[]{
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT
        };
    }

}
