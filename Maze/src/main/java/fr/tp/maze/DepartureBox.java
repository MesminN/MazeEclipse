package fr.tp.maze;

public class DepartureBox extends EmptyBox {
    public DepartureBox(Coordinates coordinates, Maze maze) {
        super(coordinates, maze);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isDeparture() {
        return true;
    }
}
