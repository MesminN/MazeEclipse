package fr.tp.maze.exceptions;

public class UnknownFileExtensionFileMazePersistenceException extends MazeReadingException {

    public UnknownFileExtensionFileMazePersistenceException(String filePath) {
        super(filePath.split(".").length == 2 ? filePath.split(".")[1]: "", "Unknown file exception");
    }
}
