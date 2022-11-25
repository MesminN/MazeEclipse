package fr.tp.maze;

public class ArrivalBox extends EmptyBox {
    public ArrivalBox(Coordinates coordinates, Maze maze) {
        super(coordinates, maze);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isArrival() {
        return true;
    }
}
