package com.ci.group20;

import com.ci.group20.maze.Maze;
import com.ci.group20.util.Coordinate;

import java.util.Stack;

/**
 * An ant that can run a maze.
 */
public class Ant {
    private Maze maze;
    private Stack<Coordinate> walkedPath;

    public Ant(Maze maze, Coordinate position) {
        this.maze = maze;
        walkedPath = new Stack<>();
        walkedPath.push(position);
    }

    public void find(Coordinate coordinate) {
    }

    public void spreadPheromone() {
    }
}
