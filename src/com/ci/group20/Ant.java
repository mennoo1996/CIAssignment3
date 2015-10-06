package com.ci.group20;

import com.ci.group20.util.Coordinate;

import java.util.Stack;

/**
 * An ant that can run a maze.
 */
public class Ant {
    private Stack<Coordinate> walkedPath;

    public Ant(Coordinate position) {
        walkedPath = new Stack<>();
        walkedPath.push(position);
    }

}
