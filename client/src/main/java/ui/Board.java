package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Arrays;
import static ui.EscapeSequences.*;
import static ui.EscapeSequences.EMPTY;

public class Board {

    public static void drawBoard(String color, ChessBoard board){
        ArrayList<String> numIndex;
        int[] rowIndexes, colIndexes;
        if(color.equalsIgnoreCase("white")){
            numIndex = new ArrayList<>(Arrays.asList("8", "7", "6", "5", "4", "3", "2", "1"));
            rowIndexes = new int[]{7, 6, 5, 4, 3, 2, 1, 0};
            colIndexes = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
        }else{
            numIndex = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8"));
            rowIndexes = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
            colIndexes = new int[]{7, 6, 5, 4, 3, 2, 1, 0};
        }
        System.out.println(ERASE_SCREEN);
        for (int r = 0; r < 8; r++) {
            int row = rowIndexes[r];
            System.out.print(numIndex.get(r) + " ");
            for (int c = 0; c < 8; c++) {
                boolean isWhiteSquare = (r + c) % 2 == 0;
                int col = colIndexes[c];
                placePieces(isWhiteSquare, row, col, color, board);
            }
            System.out.println();
        }
    }

    public static void drawHeaders(String color){
        ArrayList<String> headers;
        if(color.toLowerCase().equals("white")){
            headers = new ArrayList<>(Arrays.asList(" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "));
        }else{
            headers = new ArrayList<>(Arrays.asList(" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "));
        }
        for(String letter : headers){
            System.out.print(" " + letter);
        }
    }

    public static void placePieces(Boolean white, int row, int col, String color, ChessBoard board){
        String piece = EMPTY;
        String bgColor;
        ChessPiece.PieceType type;
        ChessPosition pos = new ChessPosition(row + 1, col + 1);
        bgColor = white ? SET_BG_COLOR_MAGENTA : SET_BG_COLOR_RED;

        ChessPiece pieceType = board.getPiece(pos);

        if(pieceType != null) {
            type = pieceType.getPieceType();
            if (type.equals(ChessPiece.PieceType.PAWN)) {
                if (board.getPiece(pos).getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                    piece = BLACK_PAWN;
                } else {
                    piece = WHITE_PAWN;
                }
            } else if (type.equals(ChessPiece.PieceType.ROOK)) {
                if (board.getPiece(pos).getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                    piece = BLACK_ROOK;
                } else {
                    piece = WHITE_ROOK;
                }
            } else if (type.equals(ChessPiece.PieceType.KNIGHT)) {
                if (board.getPiece(pos).getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                    piece = BLACK_KNIGHT;
                } else {
                    piece = WHITE_KNIGHT;
                }
            } else if(type.equals(ChessPiece.PieceType.BISHOP)) {
                if (board.getPiece(pos).getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                    piece = BLACK_BISHOP;
                } else {
                    piece = WHITE_BISHOP;
                }
            } else if(type.equals(ChessPiece.PieceType.QUEEN)) {
                if (board.getPiece(pos).getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                    piece = BLACK_QUEEN;
                } else {
                    piece = WHITE_QUEEN;
                }
            } else if(type.equals(ChessPiece.PieceType.KING)) {
                if (board.getPiece(pos).getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                    piece = BLACK_KING;
                } else {
                    piece = WHITE_KING;
                }
            }
        }

        System.out.print(bgColor);
        if (!piece.equals(EMPTY)){
            System.out.print(piece);
        }else{
            System.out.print(EMPTY);
        }
        System.out.print(RESET_BG_COLOR);
    }

    public static void run(String color, ChessBoard board){
        drawHeaders(color);
        drawBoard(color, board);
    }
}
