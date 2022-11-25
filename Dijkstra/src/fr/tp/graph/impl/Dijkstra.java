package fr.tp.graph.impl;


public class Dijkstra {

    public static ShortestPaths findShortestPaths(Graph graph,
                                                  Vertex startVertex,
                                                  Vertex endVertex,
                                                  ProcessedVertexesSet processedVertexes,
                                                  MinDistance minDistance,
                                                  Distance distance,
                                                  ShortestPaths shortestPaths) {
        processedVertexes.add(startVertex);
        Vertex pivotVertex = startVertex;
        minDistance.setMinDistance(startVertex, 0);

        for (Vertex vertex : graph.getVertexes()) {
            if (!startVertex.equals(vertex)) {
                minDistance.setMinDistance(vertex, Integer.MAX_VALUE);
            }
        }

        while (!processedVertexes.contains(endVertex) && pivotVertex != null) {
            for (Vertex succVertex : pivotVertex.getSuccessors()) {
                if (!processedVertexes.contains(succVertex)) {
                    int currentDistance = minDistance.getMinDistance(pivotVertex) + distance.getDistance(pivotVertex, succVertex);

                    if (currentDistance < minDistance.getMinDistance(succVertex)) {
                        minDistance.setMinDistance(succVertex, currentDistance);
                        shortestPaths.setPrevious(succVertex, pivotVertex);
                    }
                }
            }

            pivotVertex = minDistance.getMinDistanceVertex(processedVertexes, graph.getVertexes());
            processedVertexes.add(pivotVertex);
        }

        return shortestPaths;
    }
}
