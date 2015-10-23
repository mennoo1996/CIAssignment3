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

public class TSP {
	
	private static final String PRODUCTS_FILEPATH = "mazes/menno_products.txt";
	private static final int ANTS = 5000;
	private static final float EVAPORATION = 0.1f;
	private static final float BETA = 0.5f;
	private static final int PHEROMONE_CONSTANT = 300;
	
	public static void main(String[] args) throws EmptyStackException {
		Random random = new Random();
		int min = Integer.MAX_VALUE;
		ArrayList<Integer> minPathVertices = new ArrayList<Integer>();
		ArrayList<Coordinate> products = readProductsFile(PRODUCTS_FILEPATH);
		HashMap<CoordinatePair, Stack<Coordinate>> routes = new HashMap<CoordinatePair, Stack<Coordinate>>();
		HashMap<CoordinatePair, Integer> route_sizes = new HashMap<CoordinatePair, Integer>();
		HashMap<CoordinatePair, Float> pheromone = new HashMap<CoordinatePair, Float>();

		for (int i=0;i<products.size();i++) {
			System.out.println("OUTER LOOP " + i);
			for (int j=i+1 ; j<products.size();j++) {
				System.out.println("INNER LOOP " + j);
				if (false) {
					Stack<Coordinate> route = new Stack<Coordinate>();
					CoordinatePair pair = new CoordinatePair(products.get(i), products.get(j));
					routes.put(pair, route);
					route_sizes.put(pair, 2000);
					CoordinatePair reversedPair = new CoordinatePair(products.get(j), products.get(i));
					route_sizes.put(reversedPair, 2000);
					pheromone.put(pair, 1.0f);
					pheromone.put(reversedPair, 1.0f);
				} else {
					System.out.println("STARTING X " + products.get(i).x);
					System.out.println("STARTING Y " + products.get(i).y);
					System.out.println("ENDING X " + products.get(j).x);
					System.out.println("ENDING Y " + products.get(j).y);
					Stack<Coordinate> route = Driver.computePath(products.get(i).x, products.get(i).y, products.get(j).x, products.get(j).y, "menno", 10, 100, 400, 0.1);
					CoordinatePair pair = new CoordinatePair(products.get(i), products.get(j));
					routes.put(pair, route);
					route_sizes.put(pair, route.size());
					CoordinatePair reversedPair = new CoordinatePair(products.get(j), products.get(i));
					route_sizes.put(reversedPair, route.size());
					pheromone.put(pair, 1.0f);
					pheromone.put(reversedPair, 1.0f);
				}
			}
		}
		
//		products.clear();
//		products.add(new Coordinate(0,0));
//		products.add(new Coordinate(1, 1));
//		products.add(new Coordinate(2, 2));
//		products.add(new Coordinate(3, 3));
//		
//		route_sizes.put(new CoordinatePair(new Coordinate(0,0), new Coordinate(1, 1)), 5);
//		route_sizes.put(new CoordinatePair(new Coordinate(0,0), new Coordinate(2, 2)), 8);
//		route_sizes.put(new CoordinatePair(new Coordinate(0,0), new Coordinate(3, 3)), 15);
//		route_sizes.put(new CoordinatePair(new Coordinate(1,1), new Coordinate(2, 2)), 2);
//		route_sizes.put(new CoordinatePair(new Coordinate(1,1), new Coordinate(3, 3)), 3);
//		route_sizes.put(new CoordinatePair(new Coordinate(2,2), new Coordinate(3, 3)), 10);
//		
//		route_sizes.put(new CoordinatePair(new Coordinate(1,1), new Coordinate(0, 0)), 5);
//		route_sizes.put(new CoordinatePair(new Coordinate(2,2), new Coordinate(0, 0)), 8);
//		route_sizes.put(new CoordinatePair(new Coordinate(3,3), new Coordinate(0, 0)), 15);
//		route_sizes.put(new CoordinatePair(new Coordinate(2,2), new Coordinate(1, 1)), 2);
//		route_sizes.put(new CoordinatePair(new Coordinate(3,3), new Coordinate(1, 1)), 3);
//		route_sizes.put(new CoordinatePair(new Coordinate(3,3), new Coordinate(2, 2)), 10);
//		
//		pheromone.put(new CoordinatePair(new Coordinate(0,0), new Coordinate(1, 1)), 1.0f);
//		pheromone.put(new CoordinatePair(new Coordinate(0,0), new Coordinate(2, 2)), 1.0f);
//		pheromone.put(new CoordinatePair(new Coordinate(0,0), new Coordinate(3, 3)), 1.0f);
//		pheromone.put(new CoordinatePair(new Coordinate(1,1), new Coordinate(2, 2)), 1.0f);
//		pheromone.put(new CoordinatePair(new Coordinate(1,1), new Coordinate(3, 3)), 1.0f);
//		pheromone.put(new CoordinatePair(new Coordinate(2,2), new Coordinate(3, 3)), 1.0f);
//		
//
//		pheromone.put(new CoordinatePair(new Coordinate(1,1), new Coordinate(0, 0)), 1.0f);
//		pheromone.put(new CoordinatePair(new Coordinate(2,2), new Coordinate(0, 0)), 1.0f);
//		pheromone.put(new CoordinatePair(new Coordinate(3,3), new Coordinate(0, 0)), 1.0f);
//		pheromone.put(new CoordinatePair(new Coordinate(2,2), new Coordinate(1, 1)), 1.0f);
//		pheromone.put(new CoordinatePair(new Coordinate(3,3), new Coordinate(1, 1)), 1.0f);
//		pheromone.put(new CoordinatePair(new Coordinate(3,3), new Coordinate(2, 2)), 1.0f);
//
//	
//		
		
		
		
		// initialize default pheromone
		
		for (int i=0;i<ANTS;i++) {
			//System.out.println("START OUTER LOOP");
			ArrayList<Integer> not_visited = new ArrayList<Integer>();
			ArrayList<Integer> path = new ArrayList<Integer>();
			for (int k=0;k<products.size();k++) {
				not_visited.add(k);
			}
			
			//System.out.println("NOT VISITED INITIALIZED WITH " + not_visited);
			
			int startingLocation = random.nextInt(products.size());
			int currentLocation = startingLocation;
			not_visited.remove(not_visited.indexOf(startingLocation));
			path.add(startingLocation);
			int pathLength = 0;
			//System.out.println("AFTER REMOVING START LOCATION " + not_visited);
			for (int j=1;j<products.size();j++) {
				//System.out.println("NOT VISITED IS NOW" + not_visited);
				//System.out.println("START J LOOP");
				float totalPheromone = 0;
				for (int k=0;k<not_visited.size();k++) {
					//System.out.println("START COMPUTE PHEROMONE LOOP");
					int element = not_visited.get(k);
					
					totalPheromone += pheromone.get(new CoordinatePair(products.get(currentLocation), products.get(element))) * Math.pow(1.0/(double) route_sizes.get(new CoordinatePair(products.get(currentLocation), products.get(element))), BETA);
					
				}
				
				ArrayList<Float> cumulative_chances = new ArrayList<Float>();
				for (int k=0;k<not_visited.size();k++) {
					//System.out.println("START COMPUTE CHANCES LOOP");
					int element = not_visited.get(k);
					if (k==0) {
//						System.out.println("CUMULATIVE CHANCES" + cumulative_chances);
//						System.out.println("PHEROMONE" + pheromone);
//						System.out.println("PRODUCTS" + products);
//						System.out.println("ROUTE_SIZES" + route_sizes);
//						System.out.println("ELEMENT" + element);
//						System.out.println("PRODUCTS GET ELEMENT" + products.get(element));
//						System.out.println("CURRENTLOCATION" + currentLocation);
//						System.out.println("PRODUCTS GET CURRENTLOCATION" + products.get(currentLocation));
//						System.out.println("MATH POW ROUTE SIZE" + route_sizes.get(new CoordinatePair(products.get(currentLocation), products.get(element))));
//						System.out.println("MATH POW" + Math.pow(1.0/(double) route_sizes.get(new CoordinatePair(products.get(currentLocation), products.get(element))),  BETA));
//						System.out.println("TOTALPHEROMONE" + totalPheromone);
						cumulative_chances.add((float) (pheromone.get(new CoordinatePair(products.get(currentLocation), products.get(element))) * Math.pow(1.0/(double) route_sizes.get(new CoordinatePair(products.get(currentLocation), products.get(element))),  BETA)/totalPheromone));
						
					} else {
//						System.out.println("CUMULATIVE CHANCES" + cumulative_chances);
//						System.out.println("PHEROMONE" + pheromone);
//						System.out.println("PRODUCTS" + products);
//						System.out.println("ROUTE_SIZES" + route_sizes);
//						System.out.println("ELEMENT" + element);
//						System.out.println("PRODUCTS GET ELEMENT" + products.get(element));
//						System.out.println("CURRENTLOCATION" + currentLocation);
//						System.out.println("PRODUCTS GET CURRENTLOCATION" + products.get(currentLocation));
//						System.out.println("MATH POW ROUTE SIZE" + route_sizes.get(new CoordinatePair(products.get(currentLocation), products.get(element))));

						//System.out.println("MATH POW" + Math.pow(1.0/(double) route_sizes.get(new CoordinatePair(products.get(currentLocation), products.get(element))),  BETA));
						//System.out.println("TOTALPHEROMONE" + totalPheromone);

						cumulative_chances.add(cumulative_chances.get(k-1) + (float) (pheromone.get(new CoordinatePair(products.get(currentLocation), products.get(element))) * Math.pow(1.0/(double) route_sizes.get(new CoordinatePair(products.get(currentLocation), products.get(element))),  BETA)/totalPheromone));
						
					}
				}
				
				float decision = random.nextFloat();
				//System.out.println("DECISION" + decision);
				int k=0;
				while (decision > cumulative_chances.get(k)) {
					k++;
				}
				
				
				int vertexToGo = not_visited.get(k);
				//System.out.println("VERTEXTOGO" + vertexToGo);
				pathLength += route_sizes.get(new CoordinatePair(products.get(currentLocation), products.get(vertexToGo)));
				path.add(vertexToGo);
				not_visited.remove(not_visited.indexOf(vertexToGo));
				currentLocation = vertexToGo;
				
				System.out.println("PATH" + path);
				
				
			}
			if (pathLength<min) {
				System.out.println("UPDATE MIN PATH WITH");
				System.out.println("MIN    " + pathLength);
				System.out.println("PATH     " + path);
				min = pathLength;
				minPathVertices = path;
			}
			
			for (int p=0;p<products.size();p++) {
				for (int q = p+1;q<products.size();q++) {
					pheromone.put(new CoordinatePair(products.get(p), products.get(q)), (float) ((1.0-EVAPORATION) * pheromone.get(new CoordinatePair(products.get(p), products.get(q)))));
					pheromone.put(new CoordinatePair(products.get(q), products.get(p)), (float) ((1.0-EVAPORATION) * pheromone.get(new CoordinatePair(products.get(q), products.get(p)))));
				
				}
			}
			
			float delta_pheromone = (float) PHEROMONE_CONSTANT / (float) pathLength;
			//System.out.println("DELTA PHEROMONE" + delta_pheromone);
			for (int p = 1;p<path.size();p++) {
				pheromone.put(new CoordinatePair(products.get(path.get(p)), products.get(path.get(p-1))), (float) pheromone.get(new CoordinatePair(products.get(path.get(p)), products.get(path.get(p-1)))) + delta_pheromone);
				//System.out.println("PHEROMONE PUT" + pheromone);
			}
			
			//System.out.println("PHEROMONE" + pheromone);
			
			
				
			
			// update pheromone
			
			// set min if needed
		}
		
		System.out.println("MIN PATH" + minPathVertices);
		System.out.println("MIN" + min);
		
		Stack<Coordinate> minRoute = new Stack<Coordinate>();
		
		for (int i=1;i<minPathVertices.size();i++) {
			boolean reversed = false;
			System.out.println("PART ROUTE BEGIN" + routes.get(new CoordinatePair(products.get(minPathVertices.get(i-1)), products.get(minPathVertices.get(i)))));
			Stack<Coordinate> partRoute = routes.get(new CoordinatePair(products.get(minPathVertices.get(i-1)), products.get(minPathVertices.get(i))));
			if (partRoute == null) {
				
				partRoute = routes.get(new CoordinatePair(products.get(minPathVertices.get(i)), products.get(minPathVertices.get(i-1))));
				System.out.println("PART ROUTE AFTER NULL" + routes.get(new CoordinatePair(products.get(i), products.get(i-1))));
				reversed = true;
			}
			if (!reversed) {
				int j = partRoute.size();
				Stack<Coordinate> reversedPath = new Stack<Coordinate>();
				for (int p = 0 ; p<j ; p++) {
					reversedPath.push(partRoute.pop());
				}
				for (int p = 0; p<j; p++) {
					minRoute.push(reversedPath.pop());
				}
			} else {
				int j = partRoute.size();
				for (int p = 0;p<j;p++) {
					minRoute.push(partRoute.pop());
				}
			}
			
			System.out.println(minRoute);
			Driver.printVisualizerPath(minRoute, products.get(minPathVertices.get(0)).x, products.get(minPathVertices.get(0)).y);
			
			
		}
		
		
		
		
	}
	
	public static ArrayList<Coordinate> readProductsFile(String filepath) {
		ArrayList<Coordinate> products = new ArrayList<Coordinate>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filepath));
			String line = reader.readLine();
			String[] splitted = line.split(";");
			int amount = Integer.parseInt(splitted[0]);
			for (int i=0;i<amount;i++) {
				line = reader.readLine();
				splitted = line.split(":");
				String[] splitted2 = splitted[1].split(",");
				int x = Integer.parseInt(splitted2[0].trim());
				splitted2 = splitted2[1].split(";");
				int y = Integer.parseInt(splitted2[0].trim());
				products.add(new Coordinate(x, y));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader!=null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return products;
	}
	
	

}
