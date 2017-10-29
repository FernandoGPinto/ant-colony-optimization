package co.uk.fernandopinto.aco.algo;

/**
 * Created by Fernando on 19/09/2017.
 */
public class Location {
    private String name;
    private short x;
    private short y;

    public Location(String name, short x, short y){
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {return name;}
    public short getX() {return x;}
    public short getY() {return y;}
}
