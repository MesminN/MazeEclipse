package fr.tp.maze;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 202211200013L;
    private final int x;
    private final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
