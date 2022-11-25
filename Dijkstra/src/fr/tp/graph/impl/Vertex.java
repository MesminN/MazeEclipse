package fr.tp.graph.impl;

import java.util.Set;

public interface Vertex {

    Set<Vertex> getSuccessors();

    String getLabel();
}
