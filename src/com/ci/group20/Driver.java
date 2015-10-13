package com.ci.group20;

import com.ci.group20.maze.Maze;
import com.ci.group20.maze.MazeParser;

import java.io.IOException;

public class Driver {
	
	/*
	 * Initialize maze
		Initialize pheromone values
		For each iteration
			For each ant
				While not done
					Check which directions are viable
					Choose direction based on pheromone level (use Random)
					If reached destination
						Drop appropriate amount of pheromone on walked route
						// this should not affect the ants still to walk in this
						// iteraton. Just put all the pheromone together at the
						// end. But make sure you save it somewhere
						Set done (return to start point)
			Put all the pheromone dropped together

	 */

    // Please note that these numbers are probably bullshit, as I have
    // no idea what half of these things mean
    public static final int MAX_ITERATIONS = 10;
    public static final int NUMBER_OF_ANTS = 1000;
    public static final double PHEROMONE = 832;
    public static final double EVAPORATION = 0.5;
    public static final double CONVERGENCE_CRITERIA = 1;
    // Starting and ending point variables
    public static final int STARTING_X = 0;
    public static final int STARTING_Y = 0;
    public static final int ENDING_X = 50;
    public static final int ENDING_Y = 50;
    private static final String MAZE_NAME = "easy";

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World");
        System.out.println("Yoooooooooooo");

        MazeParser parser = new MazeParser();
        Maze m = parser.parseMaze("mazes/" + MAZE_NAME + "_maze.txt", "mazes/" + MAZE_NAME + "_coordinates.txt");
        System.out.println(m.toString());
    }

}
