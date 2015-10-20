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
            
            
            closeopenareas(idx, coordinate);
            
            
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
    
    public void closeopenareas(int idx, Coordinate coordinate){
    	
    	switch(idx){
    	
    	case 0:
    		
    		if(coordinate.x-1 >= 0){
    			Coordinate left = Coordinate.get(coordinate.x-1, coordinate.y);
    			
    			float leftup = -1f;
    			if(coordinate.y +1 < maze.size().y){
    				leftup = maze.getCellPheromone(Coordinate.get(coordinate.x-1, coordinate.y+1));
    			}
    			
    			float leftdown = -1f;
    			if(coordinate.y - 1 >= 0){
    				leftup = maze.getCellPheromone(Coordinate.get(coordinate.x-1, coordinate.y+1));
    			}
    			
    		
    			if(maze.getCellPheromone(left) >= 0){
    			
    				if(leftup < 0 && leftdown >= 0){
    				
    					closeopenareas(idx, left);
    					maze.setCellPheromone(left, -1f);
    			}
    				else if(leftdown < 0 && leftup >= 0){
    				
    					closeopenareas(idx, left);
    					maze.setCellPheromone(left, -1f);
    				
    				}
    			
    			}
    		}
    		break;
    	case 1:
    		
    		if(coordinate.x+1 < maze.size().x){
    		
    			Coordinate right = Coordinate.get(coordinate.x+1, coordinate.y);
    			
    			float rightup = -1f;
    			if(coordinate.y +1 < maze.size().y){
    				
    				rightup = maze.getCellPheromone(Coordinate.get(coordinate.x+1, coordinate.y+1));
    			}
    			
    			float rightdown = -1f;
    			if(coordinate.y -1 >= 0){
    				
    				rightdown = maze.getCellPheromone(Coordinate.get(coordinate.x+1, coordinate.y+1));
    			}
    			
    		
    			if(maze.getCellPheromone(right) >= 0){
    			
    				if(rightup < 0 && rightdown >= 0){
    				
    					closeopenareas(idx, right);
    					maze.setCellPheromone(right, -1f);
    				}
    				else if(rightdown < 0 && rightup >= 0){
    				
    					closeopenareas(idx, right);
    					maze.setCellPheromone(right, -1f);
    				
    				}    			
    			}    	
    		}
    		break;
    	case 2:	
    		
    		if(coordinate.y-1 >= 0){    		
    			
    			Coordinate down = Coordinate.get(coordinate.x, coordinate.y-1);
    			
    			float downleft = -1f;
    			if(coordinate.x-1 >= 0){
    				downleft = maze.getCellPheromone(Coordinate.get(down.x-1, down.y));
    			}

    			float downright = -1f;
    			if(coordinate.x+1 < maze.size().y){
    				downright = maze.getCellPheromone(Coordinate.get(down.x+1, down.y));
    			}
    		
    			if(maze.getCellPheromone(down) >= 0){
    			
    				if(downleft < 0 && downright >= 0){
    				
    					closeopenareas(idx, down);
    					maze.setCellPheromone(down, -1f);
    				}
    				else if(downright < 0 && downleft >= 0){
    				
    					closeopenareas(idx, down);
    					maze.setCellPheromone(down, -1f);
    				
    				}
    			}
    		}
    		break;
    	case 3:
    		
    		if(coordinate.y+1 < maze.size().y){
    		
    			Coordinate up = Coordinate.get(coordinate.x, coordinate.y+1);
    			
    			float upleft = -1f;
    			if(up.x-1 >=0){
    				upleft = maze.getCellPheromone(Coordinate.get(up.x-1, up.y));
    			}

    			float upright = -1f;
    			if(up.x+1 < maze.size().x){
    				upright = maze.getCellPheromone(Coordinate.get(up.x+1, up.y));
    			}
    			
    		
    			if(maze.getCellPheromone(up) >= 0){
    			
    				if(upleft < 0 && upright >= 0){
    				
    					closeopenareas(idx, up);
    					maze.setCellPheromone(up, -1f);
    				}
    				else if(upright < 0 && upleft >= 0){
    				
    					closeopenareas(idx, up);
    					maze.setCellPheromone(up, -1f);
    				
    				}
    			}
    		}
    		break;
    }
    	
    }
}
