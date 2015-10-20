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

    public float getCellPheromone(Coordinate coord) {
        if (coord.x < 0 || coord.x >= cells.length) {
            throw new IllegalArgumentException("Invalid x coordinate");
        }
        if (coord.y < 0 || coord.y >= cells.length) {
            throw new IllegalArgumentException("Invalid y coordinate");
        }

        return cells[coord.x][coord.y];
    }

    public void setCellPheromone(Coordinate coord, float pheromone) {
        if (coord.x < 0 || coord.x >= cells.length) {
            throw new IllegalArgumentException("Invalid x coordinate");
        }
        if (coord.y < 0 || coord.y >= cells.length) {
            throw new IllegalArgumentException("Invalid y coordinate");
        }

        cells[coord.x][coord.y] = pheromone;
    }
    
    public String toString() {
    	StringBuilder res = new StringBuilder("");

        for (float[] row : cells) {
            for (float cell : row) {
                res.append(cell);
                res.append(' ');
            }
            res.append(";\n");
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


    public void evaporate(double evaporation) {
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                if (cells[x][y] > 1e-6) {
                	if((cells[x][y]*(1- evaporation)) > 1e-6){
                		cells[x][y] *= (1 - evaporation);
                	}
                }
            }
        }
    }

    public Coordinate size() {
        if (cells.length == 0) return Coordinate.get(0, 0);
        return Coordinate.get(cells.length, cells[0].length);
    }
}
