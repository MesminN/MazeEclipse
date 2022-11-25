package fr.tp.graph.impl;

import fr.tp.graph.ProcessedVertexesSet;
import fr.tp.graph.Vertex;

import java.util.HashSet;
import java.util.Set;

public class BasicVertexesSet implements ProcessedVertexesSet {

    final Set<Vertex> processedVertexes;

    public BasicVertexesSet() {
        processedVertexes = new HashSet<>();
    }

    @Override
    public void add(Vertex vertex) {
        processedVertexes.add(vertex);
    }

    @Override
    public boolean contains(Vertex vertex) {
        return processedVertexes.contains(vertex);
    }
}
