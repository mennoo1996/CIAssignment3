package com.ci.group20;

import com.ci.group20.maze.Maze;
import com.ci.group20.util.Coordinate;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * An ant that can run a maze.
 */
public class Ant {
    private Random random;
    private Maze maze;
    private Stack<Coordinate> walkedPath;

    public Ant(Maze maze, Coordinate position) {
        this.maze = maze;
        random = new Random();
        walkedPath = new Stack<>();
        walkedPath.push(position);
    }

    public void find(Coordinate target) {
        final Coordinate mazeSize = maze.size();
        ArrayList<Coordinate> possibilities = new ArrayList<>();

        while (!walkedPath.peek().equals(target)) {
            Coordinate coordinate = walkedPath.peek();
            possibilities.clear();
            if ((coordinate.x - 1) >= 0) {
                Coordinate c = new Coordinate(coordinate.x - 1, coordinate.y);
                if (maze.getCellPheromone(c) > 1e-6) {
                    possibilities.add(c);
                }
            }
            if ((coordinate.x + 1) < mazeSize.x) {
                Coordinate c = new Coordinate(coordinate.x + 1, coordinate.y);
                if (maze.getCellPheromone(c) > 1e-6) {
                    possibilities.add(c);
                }
            }
            if ((coordinate.y - 1) >= 0) {
                Coordinate c = new Coordinate(coordinate.x, coordinate.y - 1);
                if (maze.getCellPheromone(c) > 1e-6) {
                    possibilities.add(c);
                }
            }
            if ((coordinate.y + 1) < mazeSize.y) {
                Coordinate c = new Coordinate(coordinate.x, coordinate.y + 1);
                if (maze.getCellPheromone(c) > 1e-6) {
                    possibilities.add(c);
                }
            }
            Coordinate next = possibilities.get(random.nextInt(possibilities.size()));
            walkedPath.push(next);
        }
    }

    public void spreadPheromone() {
    }
}
