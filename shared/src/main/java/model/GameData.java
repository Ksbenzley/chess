package model;

import chess.ChessBoard;

public record GameData(Integer gameID, String whiteUsername, String blackUsername, String gameName, ChessBoard board) {
}
