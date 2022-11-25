import fr.tp.graph.Dijkstra;
import fr.tp.graph.ShortestPaths;
import fr.tp.graph.Vertex;
import fr.tp.graph.impl.*;

import java.util.Iterator;

public class Main {
    public static void main( String[] args ) {
        final BasicGraph testGraph = new BasicGraph();

        final int nbNodes = 10;

        for ( int index = 0; index < nbNodes; index++ ) {
            testGraph.getVertexes().add( new BasicVertex( Integer.toString( index ), testGraph ) );
        }

        final Iterator<Vertex> vertexes = testGraph.getVertexes().iterator();
        final Vertex startVertex = vertexes.next();
        final Vertex endVertex = testGraph.getVertex( Integer.toString( nbNodes - 2 ) );
        Vertex pivotVertex = startVertex;

        while ( vertexes.hasNext() ) {
            testGraph.addEdge( pivotVertex, vertexes.next(), 1 );
        }

        final ShortestPaths shortestPaths = new BasicShortestPaths();
        final ShortestPaths paths = Dijkstra.findShortestPaths(
                testGraph,
                startVertex,
                endVertex,
                new BasicVertexesSet(),
                new BasicMinDistance(),
                testGraph,
                shortestPaths);

        for ( final Vertex vertex : paths.getShortestPath( startVertex, endVertex  ) ) {
            System.out.print(vertex.getLabel() + " -> " );
        }
    }
}