package com.ci.group20.maze;

import com.ci.group20.util.Coordinate;

/**
 * A maze.
 */
public class Maze {
    private final boolean[][] cells;
    private Coordinate start;
    private Coordinate end;

    public Maze(final boolean[][] cells, Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
        this.cells = cells;
    }

    public void setStart(Coordinate start) {
        this.start = start;
    }

    public void setEnd(Coordinate end) {
        this.end = end;
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
