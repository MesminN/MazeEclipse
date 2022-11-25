package fr.tp.maze.exceptions;

public class ErroneousDepartureNumberMazeException extends MazeReadingException {
    public ErroneousDepartureNumberMazeException(String filePath, int nbOfDepartureNodes) {
        super(filePath, "Expecting 1 departure node but found " + nbOfDepartureNodes + " instead!");
    }
}
