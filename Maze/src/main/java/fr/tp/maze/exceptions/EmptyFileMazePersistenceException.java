package fr.tp.maze.exceptions;

public class EmptyFileMazePersistenceException extends MazeReadingException {

    public EmptyFileMazePersistenceException(String filePath) {
        super(filePath, "File is empty");
    }
}
