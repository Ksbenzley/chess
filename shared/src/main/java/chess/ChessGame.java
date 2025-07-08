package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor teamTurn;
    private ChessBoard chessBoard;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        //throw new RuntimeException("Not implemented");
        if (team == ChessGame.TeamColor.WHITE){
            team = ChessGame.TeamColor.BLACK;
        }else{
            team = ChessGame.TeamColor.WHITE;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //takes the list of moves from ChessPiece and then removes invalid moves based off
        //check or checkmate
        ChessPiece myPiece = chessBoard.getPiece(startPosition);
        Collection<ChessMove> possibleMoves = myPiece.pieceMoves(chessBoard, startPosition);
        Collection<ChessMove> toRemove = new ArrayList<>();

        if(myPiece == null){
            return toRemove;
        }

        for(ChessMove move : possibleMoves){
            ChessPosition startPos = move.getStartPosition();
            ChessPosition endPos = move.getEndPosition();
            TeamColor teamColor = myPiece.getTeamColor();
            ChessBoard newBoard = chessBoard.copy();

            newBoard.addPiece(endPos, myPiece);
            newBoard.addPiece(startPos, null);

            ChessBoard ogBoard = this.chessBoard;
            chessBoard = newBoard;

            if(isInCheck(teamColor)){
                toRemove.add(move);
            }
            chessBoard = ogBoard;
        }
        possibleMoves.removeAll(toRemove);
        return possibleMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition myKingPos = null;
        Collection<ChessMove> moveList = new ArrayList<>();

        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece piece = chessBoard.getPiece(currentPosition);

                if(piece != null) {
                    ChessPiece.PieceType pieceTypeOfPiece = piece.getPieceType();
                    ChessGame.TeamColor teamColorOfPiece = piece.getTeamColor();
                    if (pieceTypeOfPiece == ChessPiece.PieceType.KING && teamColorOfPiece == teamColor){
                        myKingPos = currentPosition;
                    }
                    if (chessBoard.getPiece(currentPosition).getTeamColor() != teamColor) {
                        moveList.addAll(chessBoard.getPiece(currentPosition).pieceMoves(chessBoard, currentPosition));
                    }
                }
            }
        }

        for(ChessMove move : moveList){
            if(move.getEndPosition().equals(myKingPos)){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
        //if validMoves == empty && in check:
        //checkmate
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
        //if validMoves == empty && not in check:
        //stalemate
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(chessBoard, chessGame.chessBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, chessBoard);
    }
}
