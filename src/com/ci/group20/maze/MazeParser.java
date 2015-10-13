package com.ci.group20.maze;

import com.ci.group20.util.Coordinate;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Parser for maze files.
 */
public class MazeParser {

    public MazeParser() {

    }

    public Maze parseMaze(String mazeFileName, String coordinateFileName) throws IOException {
        InputStream mazeFileStream = null;
        BufferedReader mazeFileReader = null;
        InputStream coordinateFileStream = null;
        BufferedReader coordinateFileReader = null;

        try {
            mazeFileStream = new BufferedInputStream(new FileInputStream(mazeFileName));
            mazeFileReader = new BufferedReader(new InputStreamReader(mazeFileStream));
            coordinateFileStream = new BufferedInputStream(new FileInputStream(coordinateFileName));
            coordinateFileReader = new BufferedReader(new InputStreamReader(coordinateFileStream));

            String[] size = mazeFileReader.readLine().split(" ");
            int width = Integer.parseInt(size[0]);
            int height = Integer.parseInt(size[1]);

            Coordinate.initializePool(width, height);
            float[][] cells = new float[width][height];

            for (int y = 0; y < height; y++) {
                String[] rowCells = mazeFileReader.readLine().split(" ");
                if (rowCells.length != width) {
                    throw new IllegalArgumentException("Maze file line " + (y + 1) + " is no valid maze row");
                }

                for (int x = 0; x < width; x++) {
                    switch (rowCells[x]) {
                        case "0":
                            cells[x][y] = 0;
                            break;
                        case "1":
                            cells[x][y] = 1;
                            break;
                        default:
                            throw new IllegalArgumentException("Maze file line " + (y + 1) + " is no valid maze row");
                    }
                }
            }

            String[] startingCoords = coordinateFileReader.readLine().split(", ");
            int startX = Integer.parseInt(startingCoords[0]);
            int startY = Integer.parseInt(startingCoords[1].replace(";", ""));
            Coordinate start = Coordinate.get(startX, startY);

            String[] endingCoords = coordinateFileReader.readLine().split(", ");
            int endX = Integer.parseInt(endingCoords[0]);
            int endY = Integer.parseInt(endingCoords[1].replace(";", ""));
            Coordinate end = Coordinate.get(endX, endY);

            return new Maze(cells, start, end);
        } finally {
            if (mazeFileReader != null) {
                mazeFileReader.close();
            }
            if (mazeFileStream != null) {
                mazeFileStream.close();
            }
            if (coordinateFileReader != null) {
                coordinateFileReader.close();
            }
            if (coordinateFileStream != null) {
                coordinateFileStream.close();
            }
        }
    }

}
