package fr.tp.maze.exceptions;

public class WrongMazeSizeException extends RuntimeException {
    public WrongMazeSizeException() {
        super("Wrong Maze size. \n It must have at least 2 boxes!");
    }
}
