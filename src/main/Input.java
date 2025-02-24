package main;

import java.util.List;

public class Input {
    public final int rows, cols, numPieces;
    public final String mode;
    public final List<Piece> pieces;
    public final char[][] grid;

    public Input(int rows, int cols, int numPieces, String mode, List<Piece> pieces, char[][] grid) {
        this.rows = rows;
        this.cols = cols;
        this.numPieces = numPieces;
        this.mode = mode;
        this.pieces = pieces;
        this.grid = grid;
    }
}