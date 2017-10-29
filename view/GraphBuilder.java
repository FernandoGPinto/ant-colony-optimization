package co.uk.fernandopinto.aco.view;

import co.uk.fernandopinto.aco.algo.Location;
import com.google.inject.Inject;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Created by Fernando on 22/09/2017.
 */
public class GraphBuilder implements Builder {

    private Graph graph;
    private List<Location> locations;
    private Timer timer;

    static final String PRIMARY_CSS = "graph {fill-mode:gradient-radial;fill-color:#FFFFFF,#EEEEEE;} "
            + "edge {fill-color:#222222,#a50c06,#f87d15,#fad15f,#157778,#71d5d5,#83fcf7,#3d845a,#111111;fill-mode:dyn-plain;size:0.1;} "
            + "node {size:15px;fill-color:#222222,#a50c06,#f87d15,#fad15f,#157778,#71d5d5,#83fcf7,#3d845a,#111111;fill-mode:dyn-plain;text-alignment:at-left;"
            + "stroke-mode:plain;stroke-width:2px;stroke-color:#333333;}";

    static final String SECONDARY_CSS = "graph {fill-color:black,rgb(0,3,30);fill-mode:gradient-vertical;}"
            + "node {fill-color:white;shadow-mode:gradient-radial;shadow-color:white,rgb(0,3,30);shadow-width:7;shadow-offset:0;text-visibility-mode:hidden;}"
            + "edge {fill-color:white;size:0.1;}";


    public enum Type {

        PRIMARY(PRIMARY_CSS),
        SECONDARY(SECONDARY_CSS);

        String css;

        Type(String css) {

            this.css = css;
        }
    }

    @Inject
    public GraphBuilder(List<Location> locations) {

        this.locations = locations;
    }

    public Graph setupGraph() {

        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        graph = new SingleGraph("ACO");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.quality");

        graph.setNullAttributesAreErrors(true); //used to make sure no nulls are returned when retrieving attributes
        for(Location location:locations) {
            addNode(location);
        }
        addEdges();
        for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }
        try {
            color(graph, graph.getNode(0), new Random());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return graph;
    }

    void setStyleSheet(Type type) {

        if(graph.hasAttribute("ui.stylesheet")) graph.removeAttribute("ui.stylesheet");
        graph.addAttribute("ui.stylesheet", type.css);
    }

    private static void color(Graph g, Node start, Random random)
            throws InterruptedException {
        Iterator<Node> it = start.getDepthFirstIterator();

        for (int i = 0; i < 50; i++)
            g.stepBegins(g.getStep() + 1);

        while (it.hasNext()) {
            Node n = it.next();
            n.addAttribute("ui.color", random.nextDouble());
            g.stepBegins(g.getStep() + 1);
        }

        Iterator<Edge> it2 = g.getEdgeIterator();
        while (it2.hasNext()) {
            Edge e = it2.next();
            e.addAttribute("ui.color", random.nextDouble());
            g.stepBegins(g.getStep() + 1);
        }
    }

    public void addNode(Location location) {

        graph.addNode(location.getName());
        Node n = graph.getNode(location.getName());
        n.setAttribute("xy", location.getX(), location.getY());
    }

    public void addEdges() {

        for (int i = 0; i < graph.getNodeCount(); i++) {
            for(int j = 0; j < graph.getNodeCount(); j++) {
                if(i != j && !graph.getNode(i).hasEdgeBetween(j)) graph.addEdge(String.valueOf(i)+String.valueOf(j), i, j);
            }
        }
    }

    public void glowNodes() throws NoSuchElementException {

        timer = new Timer(30, new ActionListener() {

            double i = 8;
            char j = 'b';

            public void actionPerformed(ActionEvent evt) {

                String str;
                for(Node node:graph) {
                    str = "size: " + i + "; shadow-width: " + (i - 2) + ";";
                    node.setAttribute("ui.style", str);
                }
                fadeEdges();
                if(i<=8) j = 'b';
                if(i>=12) j = 't';
                if(j=='b') i+= 0.1;
                if(j=='t') i-= 0.1;
            }
        });
        timer.start();
    }

    public void stopGlowNodes() {

        timer.stop();
    }

    public void adjustEdges(float size, String index) {

        //edges fade to original size with each iteration
        String str = "size: " + size + ";";
        Edge edge = graph.getEdge(index);
        if(size == 3) edge.setAttribute("ui.color", 222222);
        try {
            edge.addAttribute("ui.style", str);
        } catch(NoSuchElementException e){
            e.getStackTrace();
        }
    }

    public void fadeEdges() throws NoSuchElementException{

        String fade;
        String att;
        double size = 0.05;
        for (Edge e : graph.getEachEdge()) {
            if (e.hasAttribute("ui.style")) {
                att = e.getAttribute("ui.style");
                att = att.replaceAll("[^\\d.]", ""); //isolate the digits within the size attribute
                double n = Double.parseDouble(att) - size;
                fade = "size: " + n + ";";
                e.addAttribute("ui.style", fade);
                if (att.contains("0.1")) e.removeAttribute("ui.style");
            }
        }
    }
}
