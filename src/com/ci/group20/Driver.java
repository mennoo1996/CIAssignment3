package com.ci.group20;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import com.ci.group20.maze.Maze;
import com.ci.group20.maze.MazeParser;
import com.ci.group20.util.Coordinate;

public class Driver {
	
	
    // The maximum amount of iterations that the ants will be simulated
    public static final int MAX_ITERATIONS = 100;

    // The number of ants that are simulated
    public static final int NUMBER_OF_ANTS = 100;

    // The amount of pheromone that the ants drop on their route
    public static final float PHEROMONE = 400f;

    // The fraction of pheromone that evaporates every iteration
    public static final double EVAPORATION = 0.1f;

    // The amount of steps that an ant needs to take at most for the algorithm to update its output
    public static int CONVERGENCE_CRITERIA = 2001;

    // Starting and ending point variables
    public static final int STARTING_X = 0;
    public static final int STARTING_Y = 91;

    public static final int ENDING_X = 124;
    public static final int ENDING_Y = 83;

    // The name of the maze that should be loaded
    private static final String MAZE_NAME = "hard";

    public static void main(String[] args) throws IOException {
        // Compute a path and print it
        Stack<Coordinate> result = computePath(STARTING_X, STARTING_Y, ENDING_X, ENDING_Y, MAZE_NAME, MAX_ITERATIONS, NUMBER_OF_ANTS, PHEROMONE, EVAPORATION, true);
        System.out.println("RESULT PRINT NOW");
        System.out.println(result);
    }

    /**
     * Computes a path using an ant colony optimization algorithm
     * @param STARTING_X the x coordinate of the ants' starting point
     * @param STARTING_Y the y coordinate of the ants' starting point
     * @param ENDING_X the x coordinate of the ants' target point
     * @param ENDING_Y the y coordinate of the ants' target point
     * @param MAZE_NAME the name of the maze the ants walk in
     * @param MAX_ITERATIONS the maximum amount of iterations that the ants are simulated for
     * @param NUMBER_OF_ANTS the number of ants that is simulated
     * @param PHEROMONE the amount of pheromone the ants drop on their route
     * @param EVAPORATION the fraction of pheromone that evaporates every iteration
     * @return the shortest path that has been found by the ants
     * @throws EmptyStackException this only happens when serious trouble occurs
     */
    public static Stack<Coordinate> computePath(int STARTING_X, int STARTING_Y, int ENDING_X, int ENDING_Y, String MAZE_NAME,
    		int MAX_ITERATIONS, int NUMBER_OF_ANTS, float PHEROMONE, double EVAPORATION, boolean getStartAndEndFromFile) throws EmptyStackException {

        // Load the maze from its file
    	MazeParser parser = new MazeParser();
    	Maze m;
    	try {
    		m = parser.parseMaze("mazes/" + MAZE_NAME + "_maze.txt", "mazes/" + MAZE_NAME + "_coordinates.txt");
    	}
		catch (IOException e) {
            e.printStackTrace();
            return null;
		}

        if (getStartAndEndFromFile) {
            STARTING_X = m.getStart().x;
            STARTING_Y = m.getStart().y;
            ENDING_X = m.getEnd().x;
            ENDING_Y = m.getEnd().y;
        }
        
        if (ENDING_X > m.size().x ||  ENDING_Y > m.size().y) {
            throw new IllegalArgumentException("Maze ending out of bounds");
        }

        // Create all the ants
        ArrayList<Ant> ants = new ArrayList<>(NUMBER_OF_ANTS);
        for (int i = 0; i < NUMBER_OF_ANTS; i++) {
            ants.add(new Ant(m, Coordinate.get(STARTING_X, STARTING_Y)));
        }

        Coordinate target = Coordinate.get(ENDING_X, ENDING_Y);
        System.out.println("TARGET " + target);
        int i;
        Stack<Coordinate> result = new Stack<>();

        // Preload the maze with some pheromone from a single ant to prevent all of the ants from having to find a random
        // route in the first iteration
        for (i = 0; i < 10; i++) {
            ants.get(0).find(target);
            System.out.println("PRELOAD: " + ants.get(0).spreadPheromone(PHEROMONE));
        }

        // Simulate the ants
        for (i = 0; i < MAX_ITERATIONS; i++) {
            // Let all ants find the target location
            ants.parallelStream().forEach(ant -> ant.find(target));

            System.out.println("ITER");
            // Evaporate the correct fraction of the pheromone in the maze
            m.evaporate(EVAPORATION);

            // Let all ants spread their pheromone, returning the length of their found route.
            List<Integer> lens = ants.stream().map(ant -> ant.spreadPheromone(PHEROMONE)).collect(Collectors.toList());

            // Print the average route length of all ants
            System.out.println(
                    lens.parallelStream()
                            .reduce(Integer::sum)
                            .get() / lens.size());

            // Find the shortest route of this iteration
            result = ants.parallelStream().min((ant, other) -> Integer.compare(ant.getPath().size(), other.getPath().size())).get().getPath();
            System.out.println(result.size());

            // Check if the route is shorter than the current shortest route.
            if (result.size() < CONVERGENCE_CRITERIA) {
                // If so, print the path to a file and set the new shortest route length.
            	printVisualizerPath(m, result, STARTING_X, STARTING_Y);
            	CONVERGENCE_CRITERIA = result.size();
            }
        }
        return result;
    	
    }
    
    static void printVisualizerPath(Stack<Coordinate> path, int STARTING_X, int STARTING_Y) {
    	PrintWriter writer = null;
    	try {
    		writer = new PrintWriter(new FileWriter("TSPOutput.txt"));

	    	ArrayList<String> outputLines = new ArrayList<>();
	    	outputLines.add(path.size() - 1 + ";");
	    	outputLines.add(STARTING_X + ", " + STARTING_Y + ";");
	    	Coordinate prevCoord = path.get(0);
	    	outputLines.add("take product #" + prevCoord.y + ";");
	    	for (int i = 1;i<path.size();i++) {
	    		Coordinate coord = path.get(i);
	    		if (coord.x == Integer.MAX_VALUE) {
	    			outputLines.add("take product #" + coord.y + ";");
	    		} else {
	    			if (i==0) {
		    			prevCoord = coord;
		    		} else {
		    			
		    			if (coord.x == prevCoord.x && coord.y == prevCoord.y-1) {
		    				outputLines.add("1;");
		    			} else if (coord.x == prevCoord.x && coord.y == prevCoord.y+1) {
		    				outputLines.add("3;");
		    			} else if (coord.x == prevCoord.x-1 && coord.y == prevCoord.y) {
		    				outputLines.add("2;");
		    			} else if (coord.x == prevCoord.x+1 && coord.y == prevCoord.y) {
		    				outputLines.add("0;");
		    			} else {
		    				String sizeString = outputLines.get(0);
		    				String[] splittedSize = sizeString.split(";");
		    				int size = Integer.parseInt(splittedSize[0]);
		    				size--;
		    				outputLines.remove(0);
		    				outputLines.add(0, size + ";");
		    			}
		    			prevCoord = coord;
		    		}
	    		}
	    	}
            outputLines.forEach(writer::println);
    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    		if (writer!=null) {
    			writer.close();
    		}
    	}
    }
    
    static void printVisualizerPath(Maze m, Stack<Coordinate> path, int STARTING_X, int STARTING_Y) {
    	PrintWriter writer = null;
    	try {
    		writer = new PrintWriter(new FileWriter("visualizerOutput2.txt"));
    		
	    	
	    	writer.println(path.size()-1 + ";");
	    	writer.println(STARTING_X + ", " + STARTING_Y + ";");
	    	Coordinate prevCoord = null;
	    	for (int i = 0;i<path.size();i++) {
	    		Coordinate coord = path.get(i);
	    		if (i==0) {
	    			prevCoord = coord;
	    		} else {
	    			if (coord.x == prevCoord.x && coord.y == prevCoord.y-1) {
	    				writer.println("1;");
	    			} else if (coord.x == prevCoord.x && coord.y == prevCoord.y+1) {
	    				writer.println("3;");
	    			} else if (coord.x == prevCoord.x-1 && coord.y == prevCoord.y) {
	    				writer.println("2;");
	    			} else if (coord.x == prevCoord.x+1 && coord.y == prevCoord.y) {
	    				writer.println("0;");
	    			}
	    			prevCoord = coord;
	    		}
	    		
	    	}
    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    		if (writer!=null) {
    			writer.close();
    		}
    	}
    }
    


}
