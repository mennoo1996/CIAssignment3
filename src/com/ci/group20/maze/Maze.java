package com.ci.group20.maze;

/**
 * A maze.
 */
public class Maze {
    private final boolean[][] cells;
    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;

    public Maze(final boolean[][] cells, int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.cells = cells;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public boolean isCellAccessible(int x, int y) {
        if (x < 0 || x >= cells.length) {
            throw new IllegalArgumentException("Invalid x coordinate");
        }
        if (y < 0 || y >= cells.length) {
            throw new IllegalArgumentException("Invalid y coordinate");
        }

        return cells[x][y];
    }
}
