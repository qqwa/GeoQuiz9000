package no.ntnu.tdt4240.geoquiz9000.models;

import java.util.ArrayList;

public class MapPicture implements IMap {
    public static class Location extends IMap.Location {
        public int x;
        public int y;
        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    protected String name;
    protected static IMap.MapType mapType;
    protected String rootPath;
    protected int locationCount;

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

    public MapPicture(MapToml map, String rootPath, ArrayList<String> pictures, ArrayList<String> descriptions,
                      ArrayList<Integer> locationsX, ArrayList<Integer> locationsY) {
        this.name = map.getName();
        this.mapType = map.getType();
        this.rootPath = rootPath;
        this.locationCount = map.getDataSetCount();
        this.map = map.getMetaData("map");
        this.distX = Double.parseDouble(map.getMetaData("dist_x"));
        this.distY = Double.parseDouble(map.getMetaData("dist_y"));

        this.pictures = pictures;
        this.description = descriptions;
        this.locationsX = locationsX;
        this.locationsY = locationsY;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IMap.MapType getType() {
        return mapType;
    }

    public String getMap() {
        return map;
    }

    @Override
    public String getRootPath() { return rootPath; }

    public double getDistX() {
        return distX;
    }

    public double getDistY() {
        return distY;
    }

    @Override
    public int getLocationCount() {
        return pictures.size();
    }

    public String getLocationPicture(int id) {
        return pictures.get(id);
    }

    public String getLocationDescription(int id) {
        return description.get(id);
    }

    public int getLocationX(int id) {
        return locationsX.get(id);
    }

    public int getLocationY(int id) {
        return locationsY.get(id);
    }
}
