package fr.tp.maze;

import fr.tp.graph.Vertex;
import fr.tp.maze.model.MazeBoxModel;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public abstract class MazeBox implements Vertex, MazeBoxModel, Serializable {
    private static final long serialVersionUID = 202211200015L;
    protected Coordinates coordinates;
    protected Maze maze;

    public MazeBox(Coordinates coordinates, Maze maze) {
        this.coordinates = coordinates;
        this.maze = maze;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Maze getMaze() {
        return maze;
    }

    @Override
    public Set<Vertex> getSuccessors() {
        Set<Vertex> vertices = new HashSet<>();
        for(Vertex vertex: this.maze.findNeighbors(this)) {
            vertices.add(vertex);
        }
        return vertices;
    }

    public boolean isValid() {
        return (this.coordinates.getX() >= 0 && this.coordinates.getX() < maze.getHeigth())
                && (this.coordinates.getY() >= 0 && this.coordinates.getY() < maze.getWidth());
    }

    @Override
    public boolean belongsToShortestPath() {
        return this.maze.getShortestPaths().getShortestPath(this.maze.getStartMazeBox(), this.maze.getEndMazeBox()).contains(this);
    }

    @Override
    public void setEmpty() {
        this.getMaze().setEmpty(this.getCoordinates());
    }

    @Override
    public void setWall() {
        this.getMaze().setWall(this.getCoordinates());
    }

    @Override
    public void setDeparture() {
        this.getMaze().setDeparture(this.getCoordinates());
    }

    @Override
    public void setArrival() {
        this.getMaze().setArrival(this.getCoordinates());
    }
}
