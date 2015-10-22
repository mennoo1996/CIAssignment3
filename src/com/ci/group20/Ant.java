package com.ci.group20;

import com.ci.group20.maze.Maze;
import com.ci.group20.util.Coordinate;

import java.util.ArrayList;
import java.util.Arrays;
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
    private ArrayList<String> movement;

    public Stack<Coordinate> getPath() {
        return walkedPath;
    }

    public Ant(Maze maze, Coordinate position) {
        this.maze = maze;
        random = new Random();
        startingPosition = position;
        walkedPath = new Stack<>();
        walkedPath.push(position);
        movement = new ArrayList<String>();
    }

    public void find(Coordinate target) {
        walkedPath.clear();
        walkedPath.push(startingPosition);
        final Coordinate mazeSize = maze.size();
        Coordinate[] possibilities = new Coordinate[4];
        double[] accumProbabilities = new double[4];
        double totalPheromone;

        int i = 0;
        while (!walkedPath.peek().equals(target)) {
            i++;
            Coordinate coordinate = walkedPath.pop();
            Coordinate prev = null;
            if (!walkedPath.empty()) {
                prev = walkedPath.peek();
            }
            
            /**'
             * Diego's code, to get out double laying of pheromone when walking circles
             */
            
            if(walkedPath.contains(coordinate)){
            	
            	while(!walkedPath.peek().equals(coordinate)){
            		
            		walkedPath.pop();
            		
            	}
            	walkedPath.pop();            	
            	
            }
            walkedPath.push(coordinate);

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
                if (possible != null && possible != prev) {
                    totalPheromone += Math.max(0, maze.getCellPheromone(possible));
                }
            }

            accumProbabilities[0] = ((possibilities[0] != null && possibilities[0] != prev) ? (Math.max(0, maze.getCellPheromone(possibilities[0])) / totalPheromone) : 0);
            accumProbabilities[1] = accumProbabilities[0] +
                    ((possibilities[1] != null && possibilities[1] != prev) ? (Math.max(0, maze.getCellPheromone(possibilities[1])) / totalPheromone) : 0);
            accumProbabilities[2] = accumProbabilities[1] +
                    ((possibilities[2] != null && possibilities[2] != prev) ? (Math.max(0, maze.getCellPheromone(possibilities[2])) / totalPheromone) : 0);
            accumProbabilities[3] = accumProbabilities[2] +
                    ((possibilities[3] != null && possibilities[3] != prev) ? (Math.max(0, maze.getCellPheromone(possibilities[3])) / totalPheromone) : 0);

            double prob = random.nextDouble();
            int idx;

            for (idx = 0; idx < accumProbabilities.length; idx++) {
                if (prob < accumProbabilities[idx]) {
                    break;
                }
            }

            if (idx >= possibilities.length || possibilities[idx] == null) {
                maze.setCellPheromone(coordinate, -1.f);
                walkedPath.pop();
                continue;
            }

            if (idx > 3) {
                int v = 1 + 1;
            }
            
            /**
             * Diego's code, closes off all openspaces at the left
             */
            
           /* boolean checkdirect = true;
            
            int spacesahead = 1;
            
            //System.out.println("Arrived at Diego's code");
            
            while(checkdirect == true){
            	
            	switch(idx){
            	
            	case 0:
            		
            			if(coordinate.x-spacesahead >= 0 && 
            					maze.getCellPheromone(Coordinate.get(coordinate.x-spacesahead, coordinate.y)) >= 0f && 
            					!Coordinate.get(Driver.ENDING_X, Driver.ENDING_Y).equals(Coordinate.get(coordinate.x-1, coordinate.y))&&
            					UpDownFree(coordinate.x, coordinate.y) >= 0 &&
            					UpDownFree(coordinate.x-spacesahead, coordinate.y) >= 0&&
            					UpDownFree(coordinate.x-spacesahead -1, coordinate.y) >= 0 &&
            					UpDownFree(coordinate.x-spacesahead -1, coordinate.y) == UpDownFree(coordinate.x-spacesahead, coordinate.y) &&
            					UpDownFree(coordinate.x, coordinate.y) == UpDownFree(coordinate.x-spacesahead, coordinate.y))
            			{
            				
            			    maze.setCellPheromone(Coordinate.get(coordinate.x-spacesahead, coordinate.y), -1f);   

                    		System.out.println("Closed " + Coordinate.get(coordinate.x-spacesahead, coordinate.y));
                    		spacesahead++;
            			
            			}
            			else{
            				checkdirect = false;
            			}
            		
            			break;
            	/*case 1:
            		
        			if(coordinate.x+spacesahead < maze.size().x && 
        					maze.getCellPheromone(Coordinate.get(coordinate.x+spacesahead, coordinate.y)) >= 0f && 
        					!Coordinate.get(Driver.ENDING_X, Driver.ENDING_Y).equals(Coordinate.get(coordinate.x+1, coordinate.y))&&
        					UpDownFree(coordinate.x, coordinate.y) &&
        					UpDownFree(coordinate.x+spacesahead, coordinate.y))
        			{
        				
        			    maze.setCellPheromone(Coordinate.get(coordinate.x+spacesahead, coordinate.y), -1f);   

                		System.out.println("Closed " + Coordinate.get(coordinate.x+spacesahead, coordinate.y));
                		spacesahead++;
        			
        			}
        			else{
        				checkdirect = false;
        			}
        		
        			break;*/
            		/*default:
            			checkdirect = false;
            			break;
            	}
            	//System.gc();
            }*/
            //System.out.println("Diego's while-loop break");
            
            
            
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
                maze.setCellPheromone(c, amount / (float)Math.pow(walkedPath.size(), 6) + maze.getCellPheromone(c));
            } else {
                maze.setCellPheromone(c, maze.getCellPheromone(c) * 0.999f);
            }
        }
        return walkedPath.size();
    }
    
    

    public int UpDownFree(Coordinate cord){
    	
    if(cord.x > 0 && cord.x < maze.size().x){	
    	if(cord.y-1 > 0 && cord.y+1 < maze.size().y){
    		
    		if(maze.getCellPheromone(Coordinate.get(cord.x, cord.y+1)) > 0 && maze.getCellPheromone(Coordinate.get(cord.x, cord.y-1)) < 0){
    			
    			return 0;
    			
    		}
    		else if(maze.getCellPheromone(Coordinate.get(cord.x, cord.y+1)) < 0 && maze.getCellPheromone(Coordinate.get(cord.x, cord.y-1)) > 0){
    			
    			return 1;
    			
    		}
    		else if(maze.getCellPheromone(Coordinate.get(cord.x, cord.y+1)) > 0 && maze.getCellPheromone(Coordinate.get(cord.x, cord.y-1)) > 0){
    	
    			return 2;
    		}
    	}
    }
    return -1;
    }
    
    public int UpDownFree(int x, int y){
    	
    	return UpDownFree(Coordinate.get(x, y));
    	
    }

}
