package fr.tp.maze;

import fr.tp.maze.Maze;
import fr.tp.maze.model.MazeFactory;
import fr.tp.maze.model.MazeModel;

import java.io.Serializable;

public class MazeFactoryImpl implements MazeFactory, Serializable {
    private static final long serialVersionUID = 202211201502L;

    @Override
    public MazeModel createMazeModel(int height, int width) {
        return new Maze(width, height);
    }
}
