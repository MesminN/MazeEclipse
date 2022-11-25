package fr.tp.maze;

import fr.tp.graph.Distance;
import fr.tp.graph.Vertex;

public class DistanceImpl implements Distance {
    @Override
    public int getDistance(Vertex vertex1,
                           Vertex vertex2) {
        if (vertex1.getSuccessors().contains(vertex2)) {
            return 1;
        }
        return -1;
    }
}
