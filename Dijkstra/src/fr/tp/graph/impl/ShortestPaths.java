package fr.tp.graph.impl;

import java.util.List;
import java.util.Map;

public interface ShortestPaths {

    void setPrevious(Vertex vertexEnd,
                     Vertex vertexPrevious);

    Map<Vertex, Vertex> getPreviousEdges();

    /**
     * @param startVertex
     * @param endVertex
     * @return A list of vertexes for the shortest path starting with <code>startVertex</code> and ending with <code>endVertex</code>.
     */
    List<Vertex> getShortestPath(Vertex startVertex,
                                 Vertex endVertex);
}
