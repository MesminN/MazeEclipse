package fr.tp.maze;

public class WallBox extends MazeBox {

    public WallBox(Coordinates coordinates, Maze maze) {
        super(coordinates, maze);
    }

    @Override
    public String getLabel() {
        return "W" + this.coordinates.getX() + this.coordinates.getY();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isWall() {
        return true;
    }

    @Override
    public boolean isDeparture() {
        return false;
    }

    @Override
    public boolean isArrival() {
        return false;
    }
}
