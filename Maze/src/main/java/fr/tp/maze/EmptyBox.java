package fr.tp.maze;

public class EmptyBox extends MazeBox {
    public EmptyBox(Coordinates coordinates, Maze maze) {
        super(coordinates, maze);
    }

    @Override
    public String getLabel() {
        return "E" + this.coordinates.getX() + this.coordinates.getY();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isWall() {
        return false;
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
