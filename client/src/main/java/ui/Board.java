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
                    placePieces(true, row, col);
                } else {
                    placePieces(false, row, col);
                }
            }
            System.out.println();
        }
    }

    public static void placePieces(Boolean white, int row, int col){
        String piece = EMPTY;
        String bgColor;

        if(white){
            bgColor = SET_BG_COLOR_MAGENTA;
        }else{
            bgColor = SET_BG_COLOR_RED;
        }

        if (row == 0){
            piece = switch (col) {
                case 0, 7 -> BLACK_ROOK;
                case 1, 6 -> BLACK_KNIGHT;
                case 2, 5 -> BLACK_BISHOP;
                case 3 -> BLACK_QUEEN;
                case 4 -> BLACK_KING;
                default -> EMPTY;
            };
        }else if(row == 1){
            piece = BLACK_PAWN;
        }else if (row == 6){
            piece = WHITE_PAWN;
        }else if (row == 7){
            piece = switch (col) {
                case 0, 7 -> WHITE_ROOK;
                case 1, 6 -> WHITE_KNIGHT;
                case 2, 5 -> WHITE_BISHOP;
                case 3 -> WHITE_QUEEN;
                case 4 -> WHITE_KING;
                default -> EMPTY;
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
