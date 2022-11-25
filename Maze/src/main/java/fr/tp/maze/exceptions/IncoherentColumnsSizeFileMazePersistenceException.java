package fr.tp.maze.exceptions;

public class IncoherentColumnsSizeFileMazePersistenceException extends MazeReadingException {

    public IncoherentColumnsSizeFileMazePersistenceException(final String filePath) {
        super(filePath, "Incoherent columns sizes! \n Please make sure all the columns are equal");
    }

    public IncoherentColumnsSizeFileMazePersistenceException(final String filePath, final int line) {
        super(filePath, line, "Incoherent columns sizes! \n Please make sure all the columns are equal");
    }
}
