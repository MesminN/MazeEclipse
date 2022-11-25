package fr.tp.maze;

import fr.tp.graph.Dijkstra;
import fr.tp.graph.Graph;
import fr.tp.graph.ShortestPaths;
import fr.tp.graph.Vertex;
import fr.tp.graph.impl.BasicMinDistance;
import fr.tp.graph.impl.BasicShortestPaths;
import fr.tp.graph.impl.BasicVertexesSet;
import fr.tp.maze.exceptions.ErroneousArrivalNumberMazeException;
import fr.tp.maze.exceptions.ErroneousDepartureNumberMazeException;
import fr.tp.maze.exceptions.WrongMazeSizeException;
import fr.tp.maze.model.MazeBoxModel;
import fr.tp.maze.model.MazeFactory;
import fr.tp.maze.model.MazeModel;
import fr.tp.maze.model.ModelObserver;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static fr.tp.maze.serialization.FileMazePersistenceManager.MAZE_FILE_EXTENSION;

public class Maze implements Graph, MazeModel, Serializable {
    public final static File DATA_FOLDER = new File(System.getProperty("user.dir").concat("\\data"));
    private final int width;
    private final int heigth;
    private final MazeFactory mazeFactory;
    private final List<MazeBox> mazeBoxes = new ArrayList<>();
    private transient Set<ModelObserver> observers;
    private String id;
    private ShortestPaths shortestPaths;
    private MazeBox startMazeBox;
    private MazeBox endMazeBox;

    public Maze(int width, int heigth) {
        this.checkMinimumMazeSizeOrThrow(width, heigth);
        this.width = width;
        this.heigth = heigth;
        this.observers = getObservers();
        this.shortestPaths = new BasicShortestPaths();
        this.mazeFactory = new MazeFactoryImpl();
        this.clearMaze();
        if(!DATA_FOLDER.exists()) {
            DATA_FOLDER.mkdirs();
        }
    }

    public Set<Vertex> getVertexes() {
        Set<Vertex> result = new HashSet<>();
        for(MazeBox mazeBox: this.mazeBoxes) {
            result.add(mazeBox);
        }
        return result;
    }

    @Override
    public Vertex getVertex(String label) {
        for (MazeBox mazeBox : mazeBoxes) {
            if (mazeBox.getLabel().equals(label)) {
                return mazeBox;
            }
        }
        return null;
    }

    @Override
    public Set<Vertex> getSuccessors(Vertex vertex) {
        return vertex.getSuccessors();
    }

    public Set<MazeBox> findNeighbors(MazeBox mazeBox) {
        if (!this.equals(mazeBox.getMaze()) ||
                !mazeBox.isValid() ||
                (mazeBox.isWall())) {
            return new HashSet<>();
        }

        return buildNeighboursSet(mazeBox);
    }

    private Set<MazeBox> buildNeighboursSet(MazeBox mazeBox) {
        Set<MazeBox> result = new HashSet<>();
        addIfNotNullAndNotWallBox(result, getUpNeighbor(mazeBox));
        addIfNotNullAndNotWallBox(result, getDownNeighbor(mazeBox));
        addIfNotNullAndNotWallBox(result, getLeftNeighbor(mazeBox));
        addIfNotNullAndNotWallBox(result, getRightNeighbor(mazeBox));
        return result;
    }

    private void addIfNotNullAndNotWallBox(Set<MazeBox> mazeBoxes, MazeBox mazeBox) {
        if (mazeBox != null && !(mazeBox instanceof WallBox)) {
            mazeBoxes.add(mazeBox);
        }
    }

    private MazeBox getUpNeighbor(MazeBox mazeBox) {
        int idx = this.determineIndex(mazeBox.getCoordinates()) - width;
        if (idx < 0 || idx >= mazeBoxes.size()) {
            return null;
        }
        return mazeBoxes.get(idx);
    }

    private MazeBox getDownNeighbor(MazeBox mazeBox) {
        int idx = this.determineIndex(mazeBox.getCoordinates()) + width;
        if (idx < 0 || idx >= mazeBoxes.size()) {
            return null;
        }
        return mazeBoxes.get(idx);
    }

    private MazeBox getLeftNeighbor(MazeBox mazeBox) {
        Coordinates coordinates = mazeBox.getCoordinates();
        if ((coordinates.getY() == 0)) {
            return null;
        }
        return mazeBoxes.get(this.determineIndex(coordinates) - 1);
    }

    private MazeBox getRightNeighbor(MazeBox mazeBox) {
        Coordinates coordinates = mazeBox.getCoordinates();
        if (coordinates.getY() == 9) {
            return null;
        }
        return mazeBoxes.get(this.determineIndex(coordinates) + 1);
    }

    @Override
    public MazeFactory getMazeFactory() {
        return this.mazeFactory;
    }

    public ShortestPaths getShortestPaths() {
        return shortestPaths;
    }

    public MazeBox getStartMazeBox() {
        return startMazeBox;
    }

    public MazeBox getEndMazeBox() {
        return endMazeBox;
    }

    @Override
    public void addObserver(ModelObserver modelObserver) {
        getObservers().add(modelObserver);
    }

    @Override
    public boolean removeObserver(ModelObserver modelObserver) {
        return getObservers().remove(modelObserver);
    }

    protected void notifyObserver() {
        for (final ModelObserver observer : getObservers()) {
            observer.modelStateChanged();
        }
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeigth() {
        return this.heigth;
    }

    @Override
    public MazeBoxModel getMazeBox(int rowIndex, int colIndex) {
        return this.mazeBoxes.get((rowIndex * this.width) + colIndex);
    }

    @Override
    public int getNumberOfBoxes() {
        return this.mazeBoxes.size();
    }

    @Override
    public void clearMaze() {
        this.mazeBoxes.clear();
        for (int index = 0; index < (width * heigth); index++) {
            this.mazeBoxes.add(new EmptyBox(this.determineCoordinates(index), this));
        }
        this.notifyObserver();
    }

    @Override
    public void clearShortestPath() {
        this.shortestPaths = new BasicShortestPaths();
        this.notifyObserver();
    }

    @Override
    public boolean solve() {
        this.shortestPaths = Dijkstra.findShortestPaths(this,
                startMazeBox,
                endMazeBox,
                new BasicVertexesSet(),
                new BasicMinDistance(),
                new DistanceImpl(),
                shortestPaths);

        this.notifyObserver();
        return this.isSolved();
    }

    @Override
    public List<String> validate() {
        int arrivalBoxesNb = 0, departureBoxesNb = 0;
        List<String> errors = new ArrayList<>();
        for (MazeBox mazeBox : this.mazeBoxes) {
            if (mazeBox.isDeparture()) {
                departureBoxesNb++;
            }
            if (mazeBox.isArrival()) {
                arrivalBoxesNb++;
            }
        }

        if (departureBoxesNb != 1) {
            errors.add("Error: " + departureBoxesNb + " departure node(s)");
        }

        if (arrivalBoxesNb != 1) {
            errors.add("Error: " + arrivalBoxesNb + " arrival node(s)");
        }

        return errors;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String mazeId) {
        if(mazeId != null && !mazeId.endsWith(MAZE_FILE_EXTENSION)) {
            mazeId.concat("." + MAZE_FILE_EXTENSION);
        }
        this.id = mazeId;
    }

    public void setEmpty(final Coordinates coordinates) {
        int index = this.determineIndex(coordinates);
        MazeBox mazeBox = this.mazeBoxes.get(index);
        if (!mazeBox.isEmpty()) {
            this.mazeBoxes.set(
                    index,
                    new EmptyBox(coordinates, this));
        }

        this.notifyObserver();
    }

    public void setWall(final Coordinates coordinates) {
        int index = this.determineIndex(coordinates);
        MazeBox mazeBox = this.mazeBoxes.get(index);
        if (!mazeBox.isWall()) {
            this.mazeBoxes.set(
                    index,
                    new WallBox(coordinates, this));
        }

        this.notifyObserver();
    }

    public void setDeparture(final Coordinates coordinates) {
        int index = this.determineIndex(coordinates);
        MazeBox mazeBox = this.mazeBoxes.get(index);
        if (!mazeBox.isDeparture()) {
            if (this.startMazeBox != null) {
                this.mazeBoxes.get(this.determineIndex(this.startMazeBox.getCoordinates())).setEmpty();
            }

            this.startMazeBox = new DepartureBox(coordinates, this);
            this.mazeBoxes.set(index, this.startMazeBox);
        }

        this.notifyObserver();
    }

    public void setArrival(final Coordinates coordinates) {
        int index = this.determineIndex(coordinates);
        MazeBox mazeBox = this.mazeBoxes.get(index);
        if (!mazeBox.isArrival()) {
            if (this.endMazeBox != null) {
                this.mazeBoxes.get(this.determineIndex(this.endMazeBox.getCoordinates())).setEmpty();
            }

            this.endMazeBox = new ArrivalBox(coordinates, this);
            this.mazeBoxes.set(index, this.endMazeBox);
        }
        this.notifyObserver();
    }

    public int determineIndex(final Coordinates coordinates) {
        if ((coordinates.getX() >= 0 && coordinates.getX() < this.getHeigth())
                && (coordinates.getY() >= 0 && coordinates.getY() < this.getWidth())) {
            return this.getWidth() * coordinates.getX() + coordinates.getY();
        }
        return -1;
    }

    public Coordinates determineCoordinates(final int index) {
        if (index < 0 || index >= this.mazeBoxes.size()) {
            return new Coordinates(index / this.width, index % this.width);
        }
        return null;
    }

    protected boolean isSolved() {
        List<Vertex> path = this.shortestPaths.getShortestPath(this.startMazeBox, this.endMazeBox);

        return path.contains(this.startMazeBox) && path.contains(this.endMazeBox) &&
                this.shortestPaths.getPreviousEdges().containsKey(this.endMazeBox) &&
                this.shortestPaths.getPreviousEdges().containsValue(this.startMazeBox);
    }

    public Set<ModelObserver> getObservers() {
        if(this.observers == null) {
            this.observers =  new HashSet<>();
        }
        return this.observers;
    }

    public void validateOrThrow(String filePath) throws IOException {
        int arrivalBoxesNb = 0, departureBoxesNb = 0;
        for (MazeBox mazeBox : this.mazeBoxes) {
            if (mazeBox.isDeparture()) {
                departureBoxesNb++;
            }
            if (mazeBox.isArrival()) {
                arrivalBoxesNb++;
            }
        }

        checkDepartureNodeNumberOrThrow(filePath, departureBoxesNb);
        checkArrivalNodeNumberOrThrow(filePath, arrivalBoxesNb);
    }

    public void checkDepartureNodeNumberOrThrow(final String filePath, int departureBoxesNb) throws IOException {
        if (departureBoxesNb != 1) {
            throw new ErroneousDepartureNumberMazeException(filePath, departureBoxesNb);
        }
    }

    public void checkArrivalNodeNumberOrThrow(final String filePath, int arrivalBoxesNb) throws IOException {
        if (arrivalBoxesNb != 1) {
            throw new ErroneousArrivalNumberMazeException(filePath, arrivalBoxesNb);
        }
    }

    private void checkMinimumMazeSizeOrThrow(final int width, final int height) {
        if(width+height <= 2) {
            throw new WrongMazeSizeException();
        }
    }
}
