package fr.tp.graph.impl;

import fr.tp.graph.ShortestPaths;
import fr.tp.graph.Vertex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicShortestPaths implements ShortestPaths, Serializable {
    private static final long serialVersionUID = 202211201509L;

    private final Map<Vertex, Vertex> previousEdges;

    public BasicShortestPaths() {
        previousEdges = new HashMap<>();
    }

    @Override
    public void setPrevious(Vertex vertexEnd, Vertex vertexPrevious) {
        previousEdges.put(vertexEnd, vertexPrevious);
    }


    @Override
    public List<Vertex> getShortestPath(Vertex startVertex,
                                        Vertex endVertex) {
        final List<Vertex> shortestPath = new ArrayList<>();
        Vertex previous = endVertex;

        do {
            shortestPath.add(0, previous);
            previous = previousEdges.get(previous);
        } while (previous != null && !startVertex.equals(previous));

        shortestPath.add(0, previous);

        return shortestPath;
    }

    @Override
    public Map<Vertex, Vertex> getPreviousEdges() {
        return this.previousEdges;
    }
}
