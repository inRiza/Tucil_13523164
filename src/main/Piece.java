package main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Piece {
    private List<Point> points;
    private char id;

    public Piece(List<Point> points, char id) {
        this.points = points;
        this.id = id;
    }

    public static Piece linesToPiece(List<String> lines, char id) {
        List<Point> points = new ArrayList<>();
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == id) {
                    points.add(new Point(x, y));
                }
            }
        }
        return new Piece(points, id);
    }

    public Piece rotate() {
        List<Point> rotatedPoint = new ArrayList<>();
        for (Point p : points) {
            rotatedPoint.add(new Point(-p.coordY, p.coordX));
        }
        return new Piece(rotatedPoint, id);
    }

    public Piece mirror() {
        List<Point> mirroredPoint = new ArrayList<>();
        for (Point p : points) {
            mirroredPoint.add(new Point(-p.coordX, p.coordY));
        }
        return new Piece(mirroredPoint, id);
    }

    public List<Point> getPointsAt(int a, int b) {
        return points.stream().map(p -> p.shift(a, b)).collect(Collectors.toList());
    }

    public char getId() {
        return id;
    }
}