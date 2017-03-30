package no.ntnu.tdt4240.geoquiz9000.models;

import java.util.ArrayList;

public class MapPicture extends AbstractModel implements IMap {
    private String name;
    private MapType type;

    //metaData
    private String map; //ref to map picture
    //data to calculate distances
    private double distX;
    private double distY;

    //dataSets
    private ArrayList<String> pictures;
    private ArrayList<String> description;
    private ArrayList<Integer> locationsX;
    private ArrayList<Integer> locationsY;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MapType getType() {
        return type;
    }

    //TODO: maybe return the actual map instead of a string
    public String getMap() {
        return map;
    }

    public double getDistX() {
        return distX;
    }

    public double getDistY() {
        return distY;
    }

    @Override
    public int locationsGetCount() {
        return pictures.size();
    }

    @Override
    public String locationsGetPicture(int id) {
        return pictures.get(id);
    }

    @Override
    public String locationsGetDescription(int id) {
        return description.get(id);
    }

    public int locationsGetX(int id) {
        return locationsX.get(id);
    }

    public int locationsGetY(int id) {
        return locationsY.get(id);
    }
}
