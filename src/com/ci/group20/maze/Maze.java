package com.ci.group20.maze;

import com.ci.group20.util.Coordinate;

/**
 * A maze.
 */

//Moet geen boolean zijn ivm pheromone
public class Maze {
    private final float[][] cells;
    private Coordinate start;
    private Coordinate end;

    public Maze(final float[][] cells, Coordinate start, Coordinate end) {
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

    public float getCellPheromone(int x, int y) {
        if (x < 0 || x >= cells.length) {
            throw new IllegalArgumentException("Invalid x coordinate");
        }
        if (y < 0 || y >= cells.length) {
            throw new IllegalArgumentException("Invalid y coordinate");
        }

        return cells[x][y];
    }

    public void setCellPheromone(int x, int y, float pheromone) {
        if (x < 0 || x >= cells.length) {
            throw new IllegalArgumentException("Invalid x coordinate");
        }
        if (y < 0 || y >= cells.length) {
            throw new IllegalArgumentException("Invalid y coordinate");
        }

        cells[x][y] = pheromone;
    }
    
    public String toString() {
    	StringBuilder res = new StringBuilder("");

        for (float[] row : cells) {
            for (float cell : row) {
                res.append(cell);
                res.append(' ');
            }
            res.append('\n');
        }
    	return res.toString();
    }

	/**
	 * @return the start
	 */
	public Coordinate getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public Coordinate getEnd() {
		return end;
	}
    
    
}
