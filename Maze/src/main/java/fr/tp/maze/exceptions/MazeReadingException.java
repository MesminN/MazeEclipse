package fr.tp.maze.exceptions;

import java.io.IOException;

public class MazeReadingException extends IOException {
    private final String filePath;
    private final int line;
    private final String errorMessage;

    public MazeReadingException(final String filePath, final int line, final String errorMessage) {
        super("Error in " + filePath + " at line " + line + ": \n" + errorMessage);
        this.filePath = filePath;
        this.line = line;
        this.errorMessage = errorMessage;
    }

    public MazeReadingException(String filePath, String errorMessage) {
        super("Error in " + filePath + ": \n" + errorMessage);
        this.line = 0;
        this.filePath = filePath;
        this.errorMessage = errorMessage;
    }
}
