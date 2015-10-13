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
    private Coordinate startingPosition;
    private Stack<Coordinate> walkedPath;

    public Ant(Maze maze, Coordinate position) {
        this.maze = maze;
        random = new Random();
        startingPosition = position;
        walkedPath = new Stack<>();
        walkedPath.push(position);
    }

    public void find(Coordinate target) {
        walkedPath.clear();
        walkedPath.push(startingPosition);
        final Coordinate mazeSize = maze.size();
        ArrayList<Coordinate> possibilities = new ArrayList<>();

        int i = 0;
        while (!walkedPath.peek().equals(target)) {
            i++;
            Coordinate coordinate = walkedPath.peek();
            possibilities.clear();
            if ((coordinate.x - 1) >= 0) {
                Coordinate c = Coordinate.get(coordinate.x - 1, coordinate.y);
                if (maze.getCellPheromone(c) >= 0) {
                    possibilities.add(c);
                }
            }
            if ((coordinate.x + 1) < mazeSize.x) {
                Coordinate c = Coordinate.get(coordinate.x + 1, coordinate.y);
                if (maze.getCellPheromone(c) >= 0) {
                    possibilities.add(c);
                }
            }
            if ((coordinate.y - 1) >= 0) {
                Coordinate c = Coordinate.get(coordinate.x, coordinate.y - 1);
                if (maze.getCellPheromone(c) >= 0) {
                    possibilities.add(c);
                }
            }
            if ((coordinate.y + 1) < mazeSize.y) {
                Coordinate c = Coordinate.get(coordinate.x, coordinate.y + 1);
                if (maze.getCellPheromone(c) >= 0) {
                    possibilities.add(c);
                }
            }
            Coordinate next = possibilities.get(random.nextInt(possibilities.size()));
            walkedPath.push(next);
        }
        System.gc();
        System.out.printf("Found coord in %d iteration\n", i);
    }

    public void spreadPheromone() {
    }
}
