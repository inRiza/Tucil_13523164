package main;

import java.util.*;

public class Board {
    private char[][] grid;
    private final int rows;
    private final int cols;
    private List<Point> emptyPoints;

    public Board(int rows, int cols, char[][] grid) {
        this.rows = rows;
        this.cols = cols;
        this.grid = grid;
        this.emptyPoints = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '.') {
                    emptyPoints.add(new Point(j, i));
                }
            }
        }
    }

    public boolean isFull() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '.')
                    return false;
            }
        }
        return true;
    }

    public boolean canPlacePiece(Piece piece, int x, int y) {
        List<Point> piecePoints = piece.getPointsAt(x, y);
        for (Point p : piecePoints) {
            if (p.coordX >= cols || p.coordY >= rows || p.coordX < 0 || p.coordY < 0 || grid[p.coordY][p.coordX] != '.') {
                return false;
            }
        }
        return true;
    }

    public void placePiece(Piece piece, int startX, int startY) {
        List<Point> piecePoints = piece.getPointsAt(startX, startY);
        for (Point p : piecePoints) {
            grid[p.coordY][p.coordX] = piece.getId();
            emptyPoints.remove(p);
        }
    }

    public void removePiece(Piece piece, int startX, int startY) {
        List<Point> piecePoints = piece.getPointsAt(startX, startY);
        for (Point p : piecePoints) {
            grid[p.coordY][p.coordX] = '.';
            emptyPoints.add(new Point(p.coordX, p.coordY));
        }
    }

    public String boardToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(grid[i][j]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public String boardToColoredString(ColorPack colors) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(colors.colorize(grid[i][j]));
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public List<Point> getEmptyPoints() {
        return emptyPoints;
    }
}