package fr.tp.maze.serialization;

import fr.tp.maze.Maze;
import fr.tp.maze.MazeBox;
import fr.tp.maze.exceptions.EmptyFileMazePersistenceException;
import fr.tp.maze.exceptions.IncoherentColumnsSizeFileMazePersistenceException;
import fr.tp.maze.exceptions.UnknownDataTypeSerializationFileMazePersistenceException;
import fr.tp.maze.exceptions.UnknownFileExtensionFileMazePersistenceException;
import fr.tp.maze.model.MazeModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializationFileMazePersistenceManager extends FileMazePersistenceManager {
    @Override
    protected MazeModel doRead(String mazeId) throws IOException {
        if(!mazeId.endsWith(".maze")) {
            throw new UnknownFileExtensionFileMazePersistenceException(mazeId);
        }

        Maze maze = null;
        ObjectInputStream objectInputStream = null;

        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(mazeId));
            maze = (Maze) objectInputStream.readObject();

            maze.validateOrThrow(mazeId);
            return maze;
        } catch (EOFException ex) {
            throw new EmptyFileMazePersistenceException(mazeId);
        } catch (ClassNotFoundException e) {
            throw new UnknownDataTypeSerializationFileMazePersistenceException(mazeId);
        } finally {
            if (objectInputStream != null) objectInputStream.close();
        }
    }

    @Override
    protected void doPersist(MazeModel mazeModel) throws IOException {
        final ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(mazeModel.getId()));
        outputStream.writeObject(mazeModel);
        outputStream.close();
    }

    private void checkMazeSizeOrThrow(final String filePath, final int width, final int height, final int boxesSize)
            throws IOException {
        if (boxesSize != height * width) {
            throw new IncoherentColumnsSizeFileMazePersistenceException(filePath);
        }
    }
}
