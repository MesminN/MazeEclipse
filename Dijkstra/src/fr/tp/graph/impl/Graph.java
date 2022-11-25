package fr.tp.graph.impl;

import java.util.Set;

public interface Graph {

    Set<Vertex> getVertexes();

    Vertex getVertex(String label);

    Set<Vertex> getSuccessors(Vertex vertex);
}
