package chess;

import java.util.Collection;

public abstract class PMCalculator {
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);
}
