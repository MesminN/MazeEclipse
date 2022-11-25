package fr.tp.maze.exceptions;

public class UnknownCharacterFileMazePersistenceException extends MazeReadingException {
    public UnknownCharacterFileMazePersistenceException(final String filePath, final char c, final int line, final int column) {
        super(filePath, line, "Unknown character " + c + "(" + line + ", " + column + ")");
    }
}
