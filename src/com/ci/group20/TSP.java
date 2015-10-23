package com.ci.group20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;
import java.util.EmptyStackException;

import com.ci.group20.util.Coordinate;
import com.ci.group20.util.CoordinatePair;

/**
 * Run this class for part two of the assignment - the traveling salesman problem
 * It outputs in the file TSPOutput.txt
 * You can change the maze and product file just below.
 * Just like other important constants.
 * @author Menno
 *
 */
public class TSP {
	

	private static final String MAZE = "hard";
	private static final String PRODUCTS_FILEPATH = "mazes/tsp_products.txt";
	private static final int ANTS = 20000;
	private static final float EVAPORATION = 0.1f;
	private static final float BETA = 0.5f;
	private static final int PHEROMONE_CONSTANT = 30000;

	
	public static void main(String[] args) throws EmptyStackException {
		// For random path choosing
		Random random = new Random();
		// This will contain the minimum route length at the end
		int min = Integer.MAX_VALUE;
		// This will contain the product indices at the end
		ArrayList<Integer> minPathVertices = new ArrayList<Integer>();
		// This contains the coordinates of all the products, in the order that they are in the file
		ArrayList<Coordinate> products = readProductsFile(PRODUCTS_FILEPATH);
		// This will contain the routes from all products to all other products after they are computed
		HashMap<CoordinatePair, Stack<Coordinate>> routes = new HashMap<CoordinatePair, Stack<Coordinate>>();
		// This will contain the sizes of the routes from the routes HashMap
		HashMap<CoordinatePair, Integer> route_sizes = new HashMap<CoordinatePair, Integer>();
		// This will contain the pheromone on each edge between products
		HashMap<CoordinatePair, Float> pheromone = new HashMap<CoordinatePair, Float>();

		// For each product
		for (int i=0;i<products.size();i++) {
			// For each other product
			for (int j=i+1 ; j<products.size();j++) {
				// Compute the path from product i to product j, using the code from part 1 (Driver class)
				Stack<Coordinate> route = Driver.computePath(products.get(i).x, products.get(i).y, products.get(j).x, products.get(j).y, MAZE, 10, 100, 400, 0.1, false);
				// Create the correct CoordinatePair
				CoordinatePair pair = new CoordinatePair(products.get(i), products.get(j));
				// Reversed pair because the walking order doesn't matter for size and pheromone
				CoordinatePair reversedPair = new CoordinatePair(products.get(j), products.get(i));
				// Put the calculated route in the route HashMap
				routes.put(pair, route);
				// Put the route size in the sizes HashMap
				route_sizes.put(pair, route.size());
				route_sizes.put(reversedPair, route.size());
				// Initialize pheromone for the edge between these products
				pheromone.put(pair, 1.0f);
				pheromone.put(reversedPair, 1.0f);
			
			}
		}
		
		// For each ant
		for (int i=0;i<ANTS;i++) {
			// Initialize not_visted which will contain the vertices that are not visited
			ArrayList<Integer> not_visited = new ArrayList<Integer>();
			// Initialize path which will contain the path of this ant
			ArrayList<Integer> path = new ArrayList<Integer>();
			// Add all vertices to not_visited
			for (int k=0;k<products.size();k++) {
				not_visited.add(k);
			}
			// Choose a random starting location
			int startingLocation = random.nextInt(products.size());
			// Current location is your starting location
			int currentLocation = startingLocation;
			// Remove starting location from not visited
			not_visited.remove(not_visited.indexOf(startingLocation));
			// Add starting location to path
			path.add(startingLocation);
			// Initialize path length with 0
			int pathLength = 0;
			// Loop for amountofproducts - 1 times -> in each iteration you get one product "further"
			for (int j=1;j<products.size();j++) {
				// Initialize totalPheromone with 0
				float totalPheromone = 0;
				// For each non visited product
				for (int k=0;k<not_visited.size();k++) {
					// get the product index
					int element = not_visited.get(k);
					// Add the pheromone on the edge from your current location to location "element" to the totalPheromone
					// Also takes into account the length of the edge
					totalPheromone += pheromone.get(new CoordinatePair(products.get(currentLocation), products.get(element))) * Math.pow(1.0/(double) route_sizes.get(new CoordinatePair(products.get(currentLocation), products.get(element))), BETA);
				}
				
				// This will contain the cumulative chance
				// Example: If the chances are 0.1, 0.7, 0.2 respectively,
				// this will contain [0.1, 0.8, 1]
				// (used to choose the next vertex later on
				ArrayList<Float> cumulative_chances = new ArrayList<Float>();
				// For each non-visited product
				for (int k=0;k<not_visited.size();k++) {
					// get the product index
					int element = not_visited.get(k);
					// If this is the first product
					if (k==0) {
						// Calculate the chance to choose location "element" as the next location
						// Chance calculation is done as follows:
						// (pheromoneOnThisEdge * (1/lengthOfThisEdge) ^ BETA) / totalPheromone
						cumulative_chances.add((float) (pheromone.get(new CoordinatePair(products.get(currentLocation), products.get(element))) * Math.pow(1.0/(double) route_sizes.get(new CoordinatePair(products.get(currentLocation), products.get(element))),  BETA)/totalPheromone));
					
					} else {
						// Just like above, just add to the last calculated chance to get the cumulative
						cumulative_chances.add(cumulative_chances.get(k-1) + (float) (pheromone.get(new CoordinatePair(products.get(currentLocation), products.get(element))) * Math.pow(1.0/(double) route_sizes.get(new CoordinatePair(products.get(currentLocation), products.get(element))),  BETA)/totalPheromone));
					}
				}
				
				// Choose a random float between 1 and 0
				float decision = random.nextFloat();
		
				int k=0;
				// this is the decision loop -> it loops until decision is larger than a cumulative chance. 
				// k is then the index of the next vertex in not_visited
				while (decision > cumulative_chances.get(k)) {
					k++;
				}
				
				// extract the actual product to go to
				int vertexToGo = not_visited.get(k);
				// Update the path length correctly
				pathLength += route_sizes.get(new CoordinatePair(products.get(currentLocation), products.get(vertexToGo)));
				// Add the vertex to go to to the path
				path.add(vertexToGo);
				// Remove the vertex to go to from not_visited
				not_visited.remove(not_visited.indexOf(vertexToGo));
				// current location is now the vertex to go to -> you actually go to this vertex here
				currentLocation = vertexToGo;
					
			}
			// If this ant has found the shortest path so far
			if (pathLength<min) {
				// Update minimal path length
				min = pathLength;
				// Update minimal path vertices
				minPathVertices = path;
			}
			
			// Pheromone evaporation loop: For each product
			for (int p=0;p<products.size();p++) {
				// For each other product
				for (int q = p+1;q<products.size();q++) {
					// Evaporate in both directions
					pheromone.put(new CoordinatePair(products.get(p), products.get(q)), (float) ((1.0-EVAPORATION) * pheromone.get(new CoordinatePair(products.get(p), products.get(q)))));
					pheromone.put(new CoordinatePair(products.get(q), products.get(p)), (float) ((1.0-EVAPORATION) * pheromone.get(new CoordinatePair(products.get(q), products.get(p)))));
				
				}
			}
			
			// Calculate the pheromone to be added to the edges of the path that the ant found
			// It is inversely proportional to the length of the found path
			float delta_pheromone = (float) PHEROMONE_CONSTANT / (float) pathLength;
			
			// Update pheromone loop: Add the just calculated delta_pheromone to the edges of the path that this ant found
			for (int p = 1;p<path.size();p++) {
				pheromone.put(new CoordinatePair(products.get(path.get(p)), products.get(path.get(p-1))), (float) pheromone.get(new CoordinatePair(products.get(path.get(p)), products.get(path.get(p-1)))) + delta_pheromone);
			}
		}
		
		// This will contain the full minimal route, which will be outputted to file
		Stack<Coordinate> minRoute = new Stack<Coordinate>();
		
		minRoute.push(new Coordinate(Integer.MAX_VALUE, minPathVertices.get(0)));
		
		// For each vertex in the minimal path
		for (int i=1;i<minPathVertices.size();i++) {
			// Reversed is used to check if the route stack should be read the other way around
			// Example: route was calculated from vertex 3 to vertex 7
			// But in the minimal route, we walk from vertex 7 to vertex 3
			// So we should add the coordinates in reversed order
			boolean reversed = false;
			// Get the route from the current vertex to the vertex that we came from
			Stack<Coordinate> partRoute = routes.get(new CoordinatePair(products.get(minPathVertices.get(i-1)), products.get(minPathVertices.get(i))));
			// If this got null, we have to reverse
			if (partRoute == null) {
				// Get the route with reversed access nodes
				partRoute = routes.get(new CoordinatePair(products.get(minPathVertices.get(i)), products.get(minPathVertices.get(i-1))));
				reversed = true;
			}
			// If you did not reverse the route, you should reverse the stack
			// (I know this sounds weird, but it's because Stack is last in, first out)
			if (!reversed) {
				// Reverse the stack
				int j = partRoute.size();
				Stack<Coordinate> reversedPath = new Stack<Coordinate>();
				for (int p = 0 ; p<j ; p++) {
					reversedPath.push(partRoute.pop());
				}
				// Add stack to minRoute
				for (int p = 0; p<j; p++) {
					minRoute.push(reversedPath.pop());
				}
				minRoute.push(new Coordinate(Integer.MAX_VALUE, minPathVertices.get(i)));
			} else {
				// Add stack to minRoute
				int j = partRoute.size();
				for (int p = 0;p<j;p++) {
					minRoute.push(partRoute.pop());
				}
				minRoute.push(new Coordinate(Integer.MAX_VALUE, minPathVertices.get(i)));

			}
			// Print the path in the correct format
			Driver.printVisualizerPath(minRoute, products.get(minPathVertices.get(0)).x, products.get(minPathVertices.get(0)).y);	
		}
		
		
		
		
	}
	
	
	public static ArrayList<Coordinate> readProductsFile(String filepath) {
		// Products will contain the coordinates of the products in the order that they are in the file
		ArrayList<Coordinate> products = new ArrayList<Coordinate>();
		BufferedReader reader = null;
		try {
			// Initialize reader
			reader = new BufferedReader(new FileReader(filepath));
			
			// Read and format the amount of products
			String line = reader.readLine();
			String[] splitted = line.split(";");
			int amount = Integer.parseInt(splitted[0]);
			
			// Loop amountOfProducts times
			for (int i=0;i<amount;i++) {
				// Read next product line
				line = reader.readLine();
				
				// Format and parse x coordinate
				splitted = line.split(":");
				String[] splitted2 = splitted[1].split(",");
				int x = Integer.parseInt(splitted2[0].trim());
				
				// Format and parse y coordinate
				splitted2 = splitted2[1].split(";");
				int y = Integer.parseInt(splitted2[0].trim());
				
				// Add product
				products.add(new Coordinate(x, y));
			}
		} catch (IOException e) {
			// Print stack trace if something goes wrong (e.g. file not found)
			e.printStackTrace();
		} finally {
			// Close reader if needed
			if (reader!=null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// Return the products array
		return products;
	}
	
	

}
