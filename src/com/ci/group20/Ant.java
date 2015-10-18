package com.ci.group20;

import com.ci.group20.maze.Maze;
import com.ci.group20.util.Coordinate;

import java.util.ArrayList;
import java.util.HashSet;
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
        Coordinate[] possibilities = new Coordinate[4];
        float[] accumProbabilities = new float[4];
        float totalPheromone;

        int i = 0;
        while (!walkedPath.peek().equals(target)) {
            i++;
            Coordinate coordinate = walkedPath.peek();

            if (coordinate.x - 1 >= 0) {
                possibilities[0] = Coordinate.get(coordinate.x - 1, coordinate.y);
            } else {
                possibilities[0] = null;
            }
            if (coordinate.x + 1 < mazeSize.x) {
                possibilities[1] = Coordinate.get(coordinate.x + 1, coordinate.y);
            } else {
                possibilities[1] = null;
            }
            if (coordinate.y - 1 >= 0) {
                possibilities[2] = Coordinate.get(coordinate.x, coordinate.y - 1);
            } else {
                possibilities[2] = null;
            }
            if (coordinate.y + 1 < mazeSize.y) {
                possibilities[3] = Coordinate.get(coordinate.x, coordinate.y + 1);
            } else {
                possibilities[3] = null;
            }

            totalPheromone = 0;
            for (Coordinate possible : possibilities) {
                if (possible != null) {
                    totalPheromone += Math.max(0, maze.getCellPheromone(possible));
                }
            }
            accumProbabilities[0] = (possibilities[0] != null ? Math.max(0, maze.getCellPheromone(possibilities[0])) / totalPheromone : 0);
            accumProbabilities[1] = accumProbabilities[0] +
                    (possibilities[1] != null ? Math.max(0, maze.getCellPheromone(possibilities[1])) / totalPheromone : 0);
            accumProbabilities[2] = accumProbabilities[1] +
                    (possibilities[2] != null ? Math.max(0, maze.getCellPheromone(possibilities[2])) / totalPheromone : 0);
            accumProbabilities[3] = 1;

            float prob = random.nextFloat();
            int idx;
            for (idx = 0; idx < accumProbabilities.length; idx++) {
                if (prob < accumProbabilities[idx]) {
                    break;
                }
            }

            if (idx > 3) {
                int v = 1 + 1;
            }

            walkedPath.push(possibilities[idx]);
        }
        System.gc();
        //System.out.printf("Found coord in %d iteration\n", i);
    }

    public int spreadPheromone(float amount) {
        HashSet<Coordinate> cache = new HashSet<>();
        for (Coordinate c : walkedPath) {
            if (!cache.contains(c)) {
                cache.add(c);
                synchronized (c) {
                    maze.setCellPheromone(c, amount / walkedPath.size() + maze.getCellPheromone(c));
                }
            }
        }
        return walkedPath.size();
    }
}
