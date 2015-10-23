package com.ci.group20;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

import com.ci.group20.maze.Maze;
import com.ci.group20.util.Coordinate;

/**
 * An ant that can run a maze.
 */
public class Ant {
    private Random random;
    private Maze maze;
    private Coordinate startingPosition;
    private Stack<Coordinate> walkedPath;
    private ArrayList<String> movement;
    private HashSet<Coordinate> avoid;
    private boolean efficient = false;

    // Remove all loops from the route
    private void makeEfficient() {
        if (!efficient) {
            Stack<Coordinate> route = new Stack<>();
            for (Coordinate c : walkedPath) {
                if (!route.contains(c)) {
                    route.push(c);
                } else {
                    while (!route.peek().equals(c)) {
                        route.pop();
                    }
                }
            }
            walkedPath = route;
            efficient = true;
        }
    }

    public Stack<Coordinate> getPath() {
        makeEfficient();
        return walkedPath;
    }

    public Ant(Maze maze, Coordinate position) {
        this.maze = maze;
        random = new Random();
        startingPosition = position;
        walkedPath = new Stack<>();
        walkedPath.push(position);
        movement = new ArrayList<>();
        avoid = new HashSet<>();
    }

    // Finds a route
    public void find(Coordinate target) throws EmptyStackException {
        // Reset all variables for the iteration
        efficient = false;
        avoid.clear();
        walkedPath.clear();
        walkedPath.push(startingPosition);
        final Coordinate mazeSize = maze.size();
        Coordinate[] possibilities = new Coordinate[4];
        double[] accumProbabilities = new double[4];
        double totalPheromone;

        // Keep going until the target has been reached
        while (!walkedPath.peek().equals(target)) {
            // Find the previously visited coordinate
            Coordinate coordinate = walkedPath.pop();
            Coordinate prev = null;
            if (!walkedPath.isEmpty()) {
                prev = walkedPath.peek();
            }
            walkedPath.push(coordinate);

            // Determine all accessible adjacent positions
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

            // Calculate the total amount of pheromone over all possible positions
            totalPheromone = 0;
            for (Coordinate possible : possibilities) {
                if (possible != null && !possible.equals(prev) && !avoid.contains(possible)) {
                    totalPheromone += Math.max(0, maze.getCellPheromone(possible));
                }
            }

            // Calculate the cumulative probability distribution of all possible positions
            accumProbabilities[0] = ((possibilities[0] != null && !possibilities[0].equals(prev) && !avoid.contains(possibilities[0])) ? (Math.max(0, maze.getCellPheromone(possibilities[0])) / totalPheromone) : 0);
            accumProbabilities[1] = accumProbabilities[0] +
                    ((possibilities[1] != null && !possibilities[1].equals(prev) && !avoid.contains(possibilities[1])) ? (Math.max(0, maze.getCellPheromone(possibilities[1])) / totalPheromone) : 0);
            accumProbabilities[2] = accumProbabilities[1] +
                    ((possibilities[2] != null && !possibilities[2].equals(prev) && !avoid.contains(possibilities[2])) ? (Math.max(0, maze.getCellPheromone(possibilities[2])) / totalPheromone) : 0);
            accumProbabilities[3] = accumProbabilities[2] +
                    ((possibilities[3] != null && !possibilities[3].equals(prev) && !avoid.contains(possibilities[3])) ? (Math.max(0, maze.getCellPheromone(possibilities[3])) / totalPheromone) : 0);

            // Find a next position in line with the previously calculated probabilities
            double prob = random.nextDouble();
            int idx;
            int acc = 0;
            for (idx = 0; idx < accumProbabilities.length; idx++) {
                // Keep track of the amount of positions accessible from this position
                if (possibilities[idx] != null && maze.getCellPheromone(possibilities[idx]) >= 0) {
                    acc++;
                }
                if (prob < accumProbabilities[idx]) {
                    break;
                }
            }

            // If this position is part of a dead end, avoid it for the rest of the iteration
            if (acc == 1) {
                avoid.add(coordinate);
            }

            // If no new position could be found, go back to the previous position and avoid this one for the rest
            // of the iteration.
            if (idx >= possibilities.length || possibilities[idx] == null) {
                avoid.add(coordinate);

                walkedPath.pop();
                if (walkedPath.isEmpty()) {
                    walkedPath.push(startingPosition);
                }
                continue;
            }

            // Add the newly calculated position to the path
            walkedPath.push(possibilities[idx]);
        }
    }

    // Spreads pheromone over the walked route proportional to its length
    public int spreadPheromone(float amount) {
        makeEfficient();
        HashSet<Coordinate> cache = new HashSet<>();
        for (Coordinate c : this.walkedPath) {
            if (!cache.contains(c)) {
                cache.add(c);
                maze.setCellPheromone(c, amount / (float)Math.pow(walkedPath.size(), 6) + maze.getCellPheromone(c));
            } else {
                maze.setCellPheromone(c, maze.getCellPheromone(c) * 0.999f);
            }
        }
        return this.walkedPath.size();
    }

}
