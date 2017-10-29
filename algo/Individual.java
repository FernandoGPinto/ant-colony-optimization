package co.uk.fernandopinto.aco.algo;

/**
 * Created by Fernando on 20/09/2017.
 */
public interface Individual {

    void initAnts();
    float chooseNextLocation(byte iteration);
}
