package main;

public class Point {
    public final int coordX;
    public final int coordY;

    public Point(int coordX, int coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public Point shift(int deltaX, int deltaY) {
        return new Point(this.coordX + deltaX, this.coordY + deltaY);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        return coordX == other.coordX && coordY == other.coordY;
    }

    @Override
    public int hashCode() {
        return 31 * coordX + coordY;
    }
}