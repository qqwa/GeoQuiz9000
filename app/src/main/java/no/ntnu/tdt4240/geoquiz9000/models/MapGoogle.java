package no.ntnu.tdt4240.geoquiz9000.models;

import java.util.ArrayList;

public class MapGoogle extends AbstractModel implements IMap {
    private String name;
    private MapType type;

    //metaData
    private String map;

    //dataSets
    private ArrayList<String> pictures;
    private ArrayList<String> description;
    private ArrayList<Double> locationsLatitude;
    private ArrayList<Double> locationsLongitude;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MapType getType() {
        return type;
    }

    public String getMap() { return map; }

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

    public double locationsGetLatitude(int id) {
        return locationsLatitude.get(id);
    }

    public double locationsGetLongitude(int id) {
        return locationsLongitude.get(id);
    }
}