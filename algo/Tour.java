package co.uk.fernandopinto.aco.algo;

/**
 * Created by Fernando on 19/09/2017.
 */
public class Tour implements PathBuilder{
    private float totalDistance = 0;
    private int[] paths;

    public Tour(){}

    public float getTotalDistance () {return totalDistance;}
    public void setTotalDistance (float totalDistance) {this.totalDistance += totalDistance;}
    public int[] getPaths () {return paths;}
    public void setPaths (int[] paths) {this.paths = paths;}
}
