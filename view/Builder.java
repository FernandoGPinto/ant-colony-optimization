package co.uk.fernandopinto.aco.view;

import co.uk.fernandopinto.aco.algo.Location;
import org.graphstream.graph.Graph;

/**
 * Created by Fernando on 22/09/2017.
 */
public interface Builder {

    Graph setupGraph();
    void addNode(Location location);
    void addEdges();
    void glowNodes();
    void stopGlowNodes();
    void adjustEdges(float size, String index);
    void fadeEdges();
}
