package fr.tp.maze.exceptions;

public class ErroneousArrivalNumberMazeException extends MazeReadingException {
    public ErroneousArrivalNumberMazeException(String filePath, int nbOfArrivalNodes) {
        super(filePath, "Expecting 1 arrival node but found " + nbOfArrivalNodes + " instead!");
    }
}
