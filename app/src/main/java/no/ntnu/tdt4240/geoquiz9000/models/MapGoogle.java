package no.ntnu.tdt4240.geoquiz9000.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class MapGoogle implements IMap {
    public static class Location extends IMap.Location {
        public double longitude;
        public double latitude;
        public Location(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }
    protected String name;
    protected static IMap.MapType mapType;
    protected String rootPath;
    protected int locationCount;

    //metaData
    private String map;

    //dataSets
    private ArrayList<String> pictures;
    private ArrayList<String> description;
    private ArrayList<Double> locationsLatitude;
    private ArrayList<Double> locationsLongitude;

    public MapGoogle(MapToml map, String rootPath, ArrayList<String> pictures, ArrayList<String> descriptions,
                     ArrayList<Double> locationsLatitude, ArrayList<Double> locationsLongitude) {
        this.name = map.getName();
        this.mapType = map.getType();
        this.rootPath = rootPath;
        this.locationCount = map.getDataSetCount();
        this.map = map.getMetaData("map");

        this.pictures = pictures;
        this.description = descriptions;
        this.locationsLatitude = locationsLatitude;
        this.locationsLongitude = locationsLongitude;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IMap.MapType getType() {
        return mapType;
    }

    public String getMap() { return map; }

    @Override
    public int getLocationCount() {
        return pictures.size();
    }

    @Override
    public String getRootPath() { return rootPath; }

    public String getLocationPicturePath(int id) {
        return pictures.get(id);
    }

    public String getLocationDescription(int id) {
        return description.get(id);
    }

    public double getLocationLatitude(int id) {
        return locationsLatitude.get(id);
    }

    public double getLocationLongitude(int id) {
        return locationsLongitude.get(id);
    }

    public Bitmap getLocationPicture(int id) {
        return BitmapFactory.decodeFile(rootPath + "/" + getLocationPicturePath(id));
    }
}