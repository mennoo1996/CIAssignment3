package com.ci.group20.util;

/**
 * A coordinate class.
 */
public class Coordinate {
    private static Coordinate[][] pool;

    public static void initializePool(int maxX, int maxY) {
        pool = new Coordinate[maxX + 1][maxY + 1];
        for (int x = 0; x <= maxX; x++) {
            for (int y = 0; y <= maxY; y++) {
                pool[x][y] = new Coordinate(x, y);
            }
        }
    }

    public static Coordinate get(int x, int y) {
        if (x > pool.length || y > pool[0].length) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }

        return pool[x][y];
    }

    public final int x;
    public final int y;

    public Coordinate(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (x != that.x) return false;
        return y == that.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
