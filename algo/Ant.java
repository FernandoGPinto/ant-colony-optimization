package co.uk.fernandopinto.aco.algo;

import com.google.inject.Inject;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

/**
 * Created by Fernando on 19/09/2017.
 */
public class Ant implements Callable<PathBuilder>, Individual {

    private PathBuilder tour;
    private final List<float[][]> edges;
    private int[] paths; // = number of cities
    private float[][] currentCity;

    @Inject
    public Ant(List<float[][]> edges, PathBuilder tour) {
        this.edges = edges;
        this.tour = tour;
        this.paths = new int[edges.size()];
    }

    public PathBuilder call() throws Exception {

        this.initAnts();
        for(byte i=1; i<edges.size(); i++) {
            tour.setTotalDistance(chooseNextLocation(i)); //byte i will help place cities in the right order in the path array
        }
        tour.setTotalDistance(currentCity[paths[0]][0]);
        tour.setPaths(paths);
        return tour;
    }

    public void initAnts() {
        //byte start = (byte) (edges.size() * Math.random());
        currentCity = edges.get(0);
        paths[0] = 0;
    }

    public float chooseNextLocation(byte iteration) {

        float distance = 0.0f;
        double rouletteWheel = 0.0d;
        synchronized (edges) {

            for (int i = 0; i < edges.size(); i++) {
                final int j = i;
                if (IntStream.of(paths).noneMatch(x -> x == j) && currentCity[i][0] != 0) {
                    rouletteWheel += Math.pow(currentCity[i][1], 1) * Math.pow(1.0 / currentCity[i][0], 3);
                }
            }
            rouletteWheel = rouletteWheel * Math.random();

            double wheelPosition = 0.0d;

            for (byte i = 0; i < edges.size(); i++) {
                final int j = i;
                if (IntStream.of(paths).noneMatch(x -> x == j) && currentCity[i][0] != 0) {
                    wheelPosition += Math.pow(currentCity[i][1], 1) * Math.pow(1.0 / currentCity[i][0], 3);
                } else continue;
                if (wheelPosition >= rouletteWheel) {
                    paths[iteration] = i;
                    distance = currentCity[i][0];
                    currentCity = edges.get(i);
                    break;
                }
            }
        }
        return distance;
    }
}
