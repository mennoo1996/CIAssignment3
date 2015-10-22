package com.ci.group20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import com.ci.group20.util.Coordinate;
import com.ci.group20.util.CoordinatePair;

public class TSP {
	
	private static final String PRODUCTS_FILEPATH = "mazes/tsp_products.txt";
	
	public static void main(String[] args) {
		ArrayList<Coordinate> products = readProductsFile(PRODUCTS_FILEPATH);
		HashMap<CoordinatePair, Stack<Coordinate>> routes = new HashMap<CoordinatePair, Stack<Coordinate>>();
		for (int i=0;i<products.size();i++) {
			System.out.println("OUTER LOOP " + i);
			for (int j=i+1 ; j<products.size();j++) {
				System.out.println("INNER LOOP " + j);
				Stack<Coordinate> route = Driver.computePath(products.get(i).x, products.get(i).y, products.get(j).x, products.get(j).y, "hard", 10, 100, 400, 0.1);
				CoordinatePair pair = new CoordinatePair(products.get(i), products.get(j));
				routes.put(pair, route);
			}
		}
		
		System.out.println(routes);
		
		
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
