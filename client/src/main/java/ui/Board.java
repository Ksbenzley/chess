package ui;

import ui.EscapeSequences;

import static ui.EscapeSequences.*;

public class Board {

    //Board dimensions
    private static final int BOARD_SIZE_IN_SQUARES = 3;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    //padded characters
    private static final String EMPTY = "     ";

    public void draw_board(String color){
        if(color.toLowerCase().equals("white")) {
            System.out.println(ERASE_SCREEN);
            System.out.print(SET_BG_COLOR_WHITE);
            for (int row = 0; row < 8; row++) {
                for (int line = 0; line < 2; line++) {
                    for (int col = 0; col < 8; col++) {
                        boolean isWhiteSquare = (row + col) % 2 == 0;
                        if (isWhiteSquare) {
                            System.out.print(SET_BG_COLOR_MAGENTA);
                            System.out.print(EMPTY);
                            System.out.print(RESET_BG_COLOR);
                        } else {
                            System.out.print(SET_BG_COLOR_RED);
                            System.out.print(EMPTY);
                            System.out.print(RESET_BG_COLOR);
                        }
                    }
                    System.out.println();
                }
            }
        }else{
            System.out.println(ERASE_SCREEN);
            System.out.print(SET_BG_COLOR_WHITE);
            for (int row = 0; row < 8; row++) {
                for (int line = 0; line < 2; line++) {
                    for (int col = 0; col < 8; col++) {
                        boolean isWhiteSquare = (row + col) % 2 == 0;
                        if (isWhiteSquare) {
                            System.out.print(SET_BG_COLOR_RED);
                            System.out.print(EMPTY);
                            System.out.print(RESET_BG_COLOR);
                        } else {
                            System.out.print(SET_BG_COLOR_MAGENTA);
                            System.out.print(EMPTY);
                            System.out.print(RESET_BG_COLOR);
                        }
                    }
                    System.out.println();
                }
            }
        }
    }

    public void main(String color){
        draw_board(color);
    }



}
