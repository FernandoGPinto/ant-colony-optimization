package co.uk.fernandopinto.aco;

import co.uk.fernandopinto.aco.algo.Location;
import co.uk.fernandopinto.aco.view.Home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Fernando on 20/09/2017.
 */
public class App {

    private static List<float[][]> edges = Collections.synchronizedList(new ArrayList<>());
    private static List<Location> locations = new ArrayList<>();

    public App() {}

    public App(List<Location> locations) {

        this.locations = locations;
    }

    public void createEdges() throws ExecutionException, InterruptedException {

        for(Location location:locations) {
            float temp[][] = new float[locations.size()][2];
            edges.add(temp);
            int i = 0;
            for(Location next:locations) {
                if(location != next) {
                    temp[i][0] = (float)Math.sqrt(Math.pow(Math.abs(location.getX()-next.getX()), 2) + Math.pow(Math.abs(location.getY()-next.getY()), 2));
                    temp[i][1] = 1; //pheromone
                }
                i++;
            }
        }
    }

    public List<float[][]> getEdges(){
        return edges;
    }

    public void clearEdges() {edges.clear();}

    public List<Location> getLocations(){
        return locations;
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> new Home().setVisible(true));
    }
}