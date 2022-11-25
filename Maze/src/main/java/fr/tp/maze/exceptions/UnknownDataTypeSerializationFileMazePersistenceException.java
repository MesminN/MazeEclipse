package fr.tp.maze.exceptions;

public class UnknownDataTypeSerializationFileMazePersistenceException extends MazeReadingException {
    public UnknownDataTypeSerializationFileMazePersistenceException(final String filePath) {
        super(filePath, "Unknown data type to deserialize");
    }

    public UnknownDataTypeSerializationFileMazePersistenceException(final String filePath, final int index) {
        super(filePath, index, "Unknown data type to deserialize");
    }
}
