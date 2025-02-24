package main;

import java.util.*;

public class Solver {
    private Board board;
    private List<Piece> pieces;
    private long cases;
    private long startTime;
    private ColorPack colorHandler;

    public Solver(int rows, int cols, List<Piece> pieces, char[][] initGrid) {
        this.board = new Board(rows, cols, initGrid);
        this.pieces = pieces;
        this.cases = 0;
        this.colorHandler = new ColorPack();
    }

    public boolean solve() {
        startTime = System.currentTimeMillis();
        return solvePuzzle(0);
    }

    private boolean solvePuzzle(int pieceIndex) {
        if (pieceIndex >= pieces.size()) {

            return board.isFull();
        }

        Piece piece = pieces.get(pieceIndex);
        Piece currentPiece = piece;
        List<Point> positions = new ArrayList<>(board.getEmptyPoints());
        for (int i = 0; i < 2; i++) {
            for (int r = 0; r < 4; r++) {
                for (Point p : positions) {
                    cases++;
                    if (board.canPlacePiece(currentPiece, p.coordX, p.coordY)) {
                        board.placePiece(currentPiece, p.coordX, p.coordY);
                        if (solvePuzzle(pieceIndex + 1)) {
                            return true;
                        }
                        board.removePiece(currentPiece, p.coordX, p.coordY);
                    }
                }
                currentPiece = currentPiece.rotate();
            }
            currentPiece = piece.mirror();
        }

        return false;
    }

    public long getExecutionTime() {
        return System.currentTimeMillis() - startTime;
    }

    public long getCases() {
        return cases;
    }

    public String getBoard() {
        return board.boardToString();
    }

    public String getColoredBoard() {
        return board.boardToColoredString(colorHandler);
    }

}