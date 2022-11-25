package fr.tp.maze.serialization;

import fr.tp.maze.Coordinates;
import fr.tp.maze.Maze;
import fr.tp.maze.MazeSize;
import fr.tp.maze.exceptions.*;
import fr.tp.maze.model.MazeModel;
import fr.tp.maze.model.MazePersistenceManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

import static fr.tp.maze.Maze.DATA_FOLDER;

public class FileMazePersistenceManager implements MazePersistenceManager {
    public final static String MAZE_FILE_EXTENSION = "maze";

    protected final char BOX_DEPARTURE = 'D';
    protected final char BOX_ARRIVAL = 'A';
    protected final char BOX_WALL = 'W';
    protected final char BOX_EMPTY = 'E';
    private final FileNameExtensionFilter filter;
    private Component editor;

    public FileMazePersistenceManager() {
        this.editor = null;
        filter = new FileNameExtensionFilter("Maze files only (*.maze)", MAZE_FILE_EXTENSION);
    }

    public void setEditor(Component editor) {
        this.editor = editor;
    }

    @Override
    public MazeModel read(String mazeId)
            throws IOException {
        if (mazeId == null || mazeId.isEmpty()) {
            final JFileChooser chooser = new JFileChooser(); //This class enable us to open a file explorer for a more ergonomic design.
            chooser.setFileFilter(filter);
            chooser.setCurrentDirectory(DATA_FOLDER);
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);

            final int returnVal = chooser.showOpenDialog(editor);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                final File file = chooser.getSelectedFile();
                mazeId = file.getPath();
            } else {
                throw new IOException("No file selected!");
            }
        }

        return doRead(mazeId);
    }

    protected MazeSize determineMazeSize(final String mazeId) throws IOException {
        String line;
        int width = 0, height = 0;

        BufferedReader fichier = null;

        try {
            fichier = new BufferedReader(new FileReader(mazeId));
            while ((line = fichier.readLine()) != null) {
                if (width == 0) {
                    width = line.length();
                } else {
                    if(line.length() != width) {
                        throw new IncoherentColumnsSizeFileMazePersistenceException(mazeId, height);
                    }
                }

                height++;
            }

            if(height == 0) {
                throw new EmptyFileMazePersistenceException(mazeId);
            }
            fichier.close();
            return new MazeSize(width, height);
        } finally {
            if (fichier != null) {
                fichier.close();
            }
        }
    }

    protected MazeModel doRead(final String mazeId)
            throws IOException {
        if(!mazeId.endsWith(".maze")) {
            throw new UnknownFileExtensionFileMazePersistenceException(mazeId);
        }

        BufferedReader fichier = null;
        int departureNodeNumber = 0, arrivalNodeNumber = 0;
        MazeSize mazeSize = determineMazeSize(mazeId);
        Maze maze = new Maze(mazeSize.getWidth(), mazeSize.getHeight());

        try {
            fichier = new BufferedReader(new FileReader(mazeId));
            for (int rowIdx = 0; rowIdx < mazeSize.getHeight(); rowIdx++) {
                String line = fichier.readLine();
                for (int colIdx = 0; colIdx < line.length(); colIdx++) {
                    char c = line.charAt(colIdx);
                    switch (c) {
                        case BOX_EMPTY:
                            maze.setEmpty(new Coordinates(rowIdx, colIdx));
                            break;
                        case BOX_WALL:
                            maze.setWall(new Coordinates(rowIdx, colIdx));
                            break;
                        case BOX_ARRIVAL:
                            maze.setArrival(new Coordinates(rowIdx, colIdx));
                            arrivalNodeNumber++;
                            break;
                        case BOX_DEPARTURE:
                            maze.setDeparture(new Coordinates(rowIdx, colIdx));
                            departureNodeNumber++;
                            break;
                        default:
                            throw new UnknownCharacterFileMazePersistenceException(mazeId, c, rowIdx, colIdx);
                    }
                }
            }

            maze.checkDepartureNodeNumberOrThrow(mazeId, departureNodeNumber);
            maze.checkArrivalNodeNumberOrThrow(mazeId, arrivalNodeNumber);
            return maze;
        } finally {
            if (fichier != null) {
                fichier.close();
            }
        }
    }

    @Override
    public void persist(final MazeModel mazeModel)
            throws IOException {
        String mazeId = mazeModel.getId();

        if (mazeId == null || mazeId.isEmpty()) {
            mazeId = newMazeId();

            if (mazeId == null || mazeId.isEmpty()) {
                throw new IOException("No file path was chosen!");
            }

            mazeModel.setId(mazeId);
        }

        doPersist(mazeModel);
    }

    protected void doPersist(final MazeModel mazeModel)
            throws IOException {
        BufferedWriter fichier = new BufferedWriter(new FileWriter(mazeModel.getId()));
        for (int rowIdx = 0; rowIdx < mazeModel.getHeigth(); rowIdx++) {
            for (int colIdx = 0; colIdx < mazeModel.getWidth(); colIdx++) {
                if (mazeModel.getMazeBox(rowIdx, colIdx).isArrival()) {
                    fichier.write(BOX_ARRIVAL);
                } else if (mazeModel.getMazeBox(rowIdx, colIdx).isEmpty()) {
                    fichier.write(BOX_EMPTY);
                } else if (mazeModel.getMazeBox(rowIdx, colIdx).isWall()) {
                    fichier.write(BOX_WALL);
                } else if (mazeModel.getMazeBox(rowIdx, colIdx).isDeparture()) {
                    fichier.write(BOX_DEPARTURE);
                } else {
                    throw new UnknownDataTypeSerializationFileMazePersistenceException(mazeModel.getId(), rowIdx * mazeModel.getWidth() + colIdx);
                }
            }
            fichier.newLine();
        }
        fichier.close();
    }

    @Override
    public boolean delete(MazeModel mazeModel)
            throws IOException {
        return new File(mazeModel.getId()).delete();
    }

    private String newMazeId() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(DATA_FOLDER);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        final int returnVal = chooser.showSaveDialog(editor);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File file = chooser.getSelectedFile();

            return file.getPath();
        }

        return null;
    }
}
