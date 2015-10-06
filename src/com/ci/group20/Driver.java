package com.ci.group20;

import com.ci.group20.maze.Maze;
import com.ci.group20.maze.MazeParser;

import java.io.IOException;

public class Driver {

    private static final String MAZE_NAME = "easy";

	public static void main(String[] args) throws IOException {
		System.out.println("Hello World");
		System.out.println("Yoooooooooooo");

        MazeParser parser = new MazeParser();
        Maze m = parser.parseMaze("mazes/" + MAZE_NAME + "_maze.txt", "mazes/" + MAZE_NAME + "_coordinates.txt");

		//Bla

	}

}
