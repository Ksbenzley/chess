package ui;

import java.util.ArrayList;
import java.util.Arrays;
import static ui.EscapeSequences.*;
import static ui.EscapeSequences.EMPTY;

public class Board {

    public static void drawBoard(String color){
        ArrayList<String> numIndex;
        if(color.equalsIgnoreCase("white")){
            numIndex = new ArrayList<>(Arrays.asList("8", "7", "6", "5", "4", "3", "2", "1"));
        }else{
            numIndex = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8"));
        }
        System.out.println(ERASE_SCREEN);
        for (int row = 0; row < 8; row++) {
            System.out.print(numIndex.get(row) + " ");
            for (int col = 0; col < 8; col++) {
                boolean isWhiteSquare = (row + col) % 2 == 0;
                if (isWhiteSquare) {
                    placePieces(true, row, col, color);
                } else {
                    placePieces(false, row, col, color);
                }
            }
            System.out.println();
        }
    }

    public static void placePieces(Boolean white, int row, int col, String color){
        String piece = EMPTY;
        String bgColor;

        bgColor = white ? SET_BG_COLOR_MAGENTA : SET_BG_COLOR_RED;

        if (row == 0){
            switch (col) {
                case 0, 7:
                    piece = color.equalsIgnoreCase("white") ? WHITE_ROOK : BLACK_ROOK;
                    break;
                case 1, 6:
                    piece = color.equalsIgnoreCase("white") ? WHITE_KING: BLACK_KNIGHT;
                    break;
                case 2, 5:
                    piece = color.equalsIgnoreCase("white") ? WHITE_BISHOP : BLACK_BISHOP;
                    break;
                case 3:
                    piece = color.equalsIgnoreCase("white") ? WHITE_QUEEN : BLACK_KING;
                    break;
                case 4:
                    piece = color.equalsIgnoreCase("white") ? WHITE_KING : BLACK_QUEEN;
                    break;
                default:
                    piece = EMPTY;
                    break;
            };
        }else if(row == 1){
            piece = color.equalsIgnoreCase("white") ? WHITE_PAWN : BLACK_PAWN;
        }else if (row == 6){
            piece = color.equalsIgnoreCase("white") ? BLACK_PAWN : WHITE_PAWN;
        }else if (row == 7){
            switch (col) {
                case 0, 7:
                    piece = color.equalsIgnoreCase("white") ? BLACK_ROOK : WHITE_ROOK;
                    break;
                case 1, 6:
                    piece = color.equalsIgnoreCase("white") ? BLACK_KNIGHT : WHITE_KNIGHT;
                    break;
                case 2, 5:
                    piece = color.equalsIgnoreCase("white") ? BLACK_BISHOP : WHITE_BISHOP;
                    break;
                case 3:
                    piece = color.equalsIgnoreCase("white") ? BLACK_QUEEN : WHITE_KING;
                    break;
                case 4:
                    piece = color.equalsIgnoreCase("white") ? BLACK_KING : WHITE_QUEEN;
                    break;
                default:
                    piece = EMPTY;
                    break;
            };
        }
        System.out.print(bgColor);
        if (!piece.equals(EMPTY)){
            System.out.print(piece);
        }else{
            System.out.print(EMPTY);
        }
        System.out.print(RESET_BG_COLOR);
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

    public static void run(String color){
        drawHeaders(color);
        drawBoard(color);
    }
}
