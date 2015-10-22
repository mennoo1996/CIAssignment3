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
    public static final int NUMBER_OF_ANTS = 100;
    public static final float PHEROMONE = 400f;
    public static final double EVAPORATION = 0.1f;
    public static final double CONVERGENCE_CRITERIA = 1;
    // Starting and ending point variables
    public static final int STARTING_X = 0;
    public static final int STARTING_Y = 0;
    public static final int ENDING_X = 58;
    public static final int ENDING_Y = 55;
    private static final String MAZE_NAME = "hard";

    public static void main(String[] args) throws IOException {
        Stack<Coordinate> result = computePath(STARTING_X, STARTING_Y, ENDING_X, ENDING_Y, MAZE_NAME, MAX_ITERATIONS, NUMBER_OF_ANTS, PHEROMONE, EVAPORATION);
        System.out.println("RESULT PRINT NOW");
        System.out.println(result);
        
    	

    }
    
    public static Stack<Coordinate> computePath(int STARTING_X, int STARTING_Y, int ENDING_X, int ENDING_Y, String MAZE_NAME,
    		int MAX_ITERATIONS, int NUMBER_OF_ANTS, float PHEROMONE, double EVAPORATION) throws EmptyStackException {
    	
    	MazeParser parser = new MazeParser();
    	Maze m = null;
    	try {
    		m = parser.parseMaze("mazes/" + MAZE_NAME + "_maze.txt", "mazes/" + MAZE_NAME + "_coordinates.txt");
    	}
    		
		catch (IOException e) {
			System.out.println(e.getStackTrace());
		}
        //System.out.println(m.toString());

        
        
        if (ENDING_X > m.size().x ||  ENDING_Y > m.size().y) {
            throw new IllegalArgumentException("Maze ending out of bounds");
        }

        ArrayList<Ant> ants = new ArrayList<>(NUMBER_OF_ANTS);
        for (int i = 0; i < NUMBER_OF_ANTS; i++) {
            ants.add(new Ant(m, Coordinate.get(STARTING_X, STARTING_Y)));
        }

        Coordinate target = Coordinate.get(ENDING_X, ENDING_Y);
        System.out.println("TARGET " + target);
        int i;
        Stack<Coordinate> result = new Stack<>();

        for (i = 0; i < 10; i++) {
            ants.get(0).find(target);
            System.out.println("PRELOAD: " + ants.get(0).spreadPheromone(PHEROMONE));
        }
        //System.out.println(m);
        
        // EDIT THIS FOR OTHER MAZES
        int absoluteMin = 2001;

        for (i = 0; i < MAX_ITERATIONS; i++) {
            ants.parallelStream().forEach(
                    (Ant ant) -> {
                        ant.find(target);
                    }
            );
            System.out.println("ITER");
            m.evaporate(EVAPORATION);
            List<Integer> lens = ants.stream().map(
                    (Ant ant) -> {
                        return ant.spreadPheromone(PHEROMONE);
                    }
            ).collect(Collectors.toList());
            System.out.println(
                    lens.parallelStream()
                            .reduce(Integer::sum)
                            .get() / lens.size());
            int min = lens.parallelStream()
                    .min(Integer::compare)
                    .get();
            System.out.println(min);
            result = ants.parallelStream().min((ant, other) -> Integer.compare(ant.getPath().size(), other.getPath().size())).get().getPath();
            //printPath(m, result);
            if (min < absoluteMin) {
            	printVisualizerPath(m, result);
            	absoluteMin = min;
            }
            
            if (min < 1500) {
                printPath(m, result);
            }
            // Menno: Outcommented the break for tsp.
            //if (min < 300) break;
           

           /*if (lens.parallelStream().filter((x) -> x <= Math.abs(ENDING_X - STARTING_X) + Math.abs(ENDING_Y - STARTING_Y) + 10).count() > 0) {
                System.out.println("FOUND OPTIMUM");
                break;
            }*/

            //System.out.println(m);
        }
        System.out.println("CONVERGED IN " + i);
        for (Coordinate c : result) {
            System.out.println(c);

        }
        return result;
    	
    }

    static void printPath(Maze m, Stack<Coordinate> path) {
        int[][] out = new int[m.size().x][m.size().y];

        for (Coordinate c : path) {
            out[c.x][c.y] += 1;
        }

        StringBuilder res = new StringBuilder("");

        for (int[] row : out) {
            for (int cell : row) {
                res.append(cell);
                res.append(' ');
            }
            res.append(";\n");
        }
        //System.out.println(res.toString());
    }
    
    static void printVisualizerPath(Maze m, Stack<Coordinate> path) {
    	PrintWriter writer = null;
    	try {
    		writer = new PrintWriter(new FileWriter("visualizerOutput2.txt"));
    		
	    	
	    	writer.println(path.size() + ";");
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
    		
    	} finally {
    		if (writer!=null) {
    			writer.close();
    		}
    	}
    }
    


}
