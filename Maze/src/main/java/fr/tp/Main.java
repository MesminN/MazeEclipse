package fr.tp;

import fr.tp.maze.serialization.FileMazePersistenceManager;
import fr.tp.maze.MazeFactoryImpl;
import fr.tp.maze.serialization.SerializationFileMazePersistenceManager;
import fr.tp.maze.model.MazeFactory;
import fr.tp.maze.model.MazeModel;
import fr.tp.maze.ui.MazeEditor;

public class Main {
    public static void main(String[] args) {
        MazeFactory mazeFactory = new MazeFactoryImpl();
        FileMazePersistenceManager mazePersistenceManager = new SerializationFileMazePersistenceManager();
        final MazeModel maze = mazeFactory.createMazeModel(10, 10);
        MazeEditor mazeEditor = new MazeEditor(maze, mazePersistenceManager);
        mazePersistenceManager.setEditor(mazeEditor);
    }
}
