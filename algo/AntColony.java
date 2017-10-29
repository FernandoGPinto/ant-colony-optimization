package co.uk.fernandopinto.aco.algo;

import co.uk.fernandopinto.aco.view.GraphBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Fernando on 20/09/2017.
 */
public class AntColony extends SwingWorker<Future<PathBuilder>, Future<PathBuilder>> {

    private List<float[][]> edges = Collections.synchronizedList(new ArrayList<>());
    private List<Ant> colony = new ArrayList<>();
    private Future<PathBuilder> iterationBest;
    private Future<PathBuilder> globalBest;
    private byte colonySize;
    private short maxIterations;
    private GraphBuilder graph;

    public AntColony(){}

    public AntColony(short maxIterations, List<float[][]> edges, GraphBuilder graph) {

        this.maxIterations = maxIterations;
        this.edges = edges;
        this.graph = graph;
        colonySize = (byte)edges.size();
    }

    private void createAnts() {

        colony.clear();
        Injector injector = Guice.createInjector(new ProjectModule());
        for(byte i=0; i<colonySize; i++) {
            colony.add(injector.getInstance(Ant.class));
        }
    }

    private void generateTours() {

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(colonySize);
        iterationBest = null;

        for (Ant ant:colony) {
            try {
                Future<PathBuilder> result = executor.submit(ant);
                if(iterationBest == null || result.get().getTotalDistance() < iterationBest.get().getTotalDistance()) {
                    iterationBest = result;
                }
                if(globalBest == null || globalBest.get().getTotalDistance() > iterationBest.get().getTotalDistance()){
                    globalBest = iterationBest;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }

    private void updatePheromone(int iteration) throws ExecutionException, InterruptedException {
        //MaxMin method (MMAS)
        float additionalPheromone;
        int[] paths;
        float maxPheromone;
        float minPheromone;
        float nthRoot = (float)Math.pow(0.05f, 1f/edges.size());
        //params used: rho = 0.98; pbest = 0.05; switch from iterationBest to globalBest = 75% of iterations completed
        if(iteration/maxIterations <= 0.75) {
            additionalPheromone = 1f/iterationBest.get().getTotalDistance();
            paths = iterationBest.get().getPaths();
            maxPheromone = 1f/(iterationBest.get().getTotalDistance()-(0.98f*iterationBest.get().getTotalDistance()));
        } else {
            additionalPheromone = 1f/globalBest.get().getTotalDistance();
            paths = globalBest.get().getPaths();
            maxPheromone = 1f/(globalBest.get().getTotalDistance()-(0.98f*globalBest.get().getTotalDistance()));
        }
        minPheromone = maxPheromone*(1f-nthRoot)/((edges.size()/2f-1f)*nthRoot);
        //evaporate pheromone
        for(float[][] current:edges) {
            for(float[] pheromone:current){
                pheromone[1] *= 0.98f;
            }
        }
        //add pheromone
        float[][] location;
        for(int i = 0; i < paths.length-1; i++) {
            location = edges.get(paths[i]);
            location[paths[i+1]][1] += additionalPheromone;
            location = edges.get(paths[i+1]);
            location[paths[i]][1] += additionalPheromone;
            // ********** wrap around at the end ************
        }
        //constrain pheromone between min and max
        for(float[][] current:edges) {
            for(float[] pheromone:current){
                if(pheromone[1] > maxPheromone) pheromone[1] = maxPheromone;
                if(pheromone[1] < minPheromone) pheromone[1] = minPheromone;
            }
        }
    }

    private void updateGraph(int[] arr, float size) {

        for(int j=0; j<arr.length-1; j++){
            String index = String.valueOf(arr[j])+String.valueOf(arr[j+1]);
            if(arr[j]>arr[j+1]) index = String.valueOf(arr[j+1])+String.valueOf(arr[j]);
            graph.adjustEdges(size, index);
        }
        String index = String.valueOf(arr[0])+String.valueOf(arr[arr.length-1]);
        if(arr[0]>arr[arr.length-1]) index = String.valueOf(arr[arr.length-1])+String.valueOf(arr[0]);
        graph.adjustEdges(size, index);
    }

    @Override
    protected Future<PathBuilder> doInBackground() {

        try {
            for (int i = 0; i < maxIterations; i++) {
                createAnts();
                generateTours();
                updatePheromone(i);
                publish(iterationBest);
                publish(globalBest);
            }
            graph.stopGlowNodes();
            publish(globalBest);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return globalBest;
    }

    @Override
    protected void process(List<Future<PathBuilder>> chunks) {

        for (Future<PathBuilder> tour : chunks) {
            try {
                updateGraph(tour.get().getPaths(), 1.5f);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
