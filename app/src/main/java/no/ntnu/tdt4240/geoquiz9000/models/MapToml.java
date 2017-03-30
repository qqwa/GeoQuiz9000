package no.ntnu.tdt4240.geoquiz9000.models;


import android.util.Log;
import com.moandjiezana.toml.Toml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapToml {
    public static class DataSet {
        public String picture;
        public String description;
        public Location location;

        public DataSet(String picture, String description, Location location) {
            this.picture = picture;
            this.description = description;
            this.location = location;
        }
    }
    public static abstract class Location {};
    public static class LocationGoogle extends Location {
        public double longitude;
        public double latitude;
        public LocationGoogle(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }
    public static class LocationPicture extends Location {
        public int x;
        public int y;
        public LocationPicture(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    private String name;
    private IMap.MapType type;
    private Map<String, String> metaData;
    private ArrayList<DataSet> dataSets;

    public MapToml(String name, IMap.MapType type, Map metaData, ArrayList<DataSet> dataSets) {
        this.name = name;
        this.type = type;
        this.metaData = metaData;
        this.dataSets = dataSets;
    }

    static public MapToml readToml(InputStream inputStream) {
        Toml toml = new Toml().read(inputStream);

        //get toplevel data
        String name = toml.getString("name");
        String type = toml.getString("type");

        if(name == null || type == null) {
            throw new RuntimeException("Couldn't parse toml file");
        }

        IMap.MapType mapType;

        if(type.equals("google-maps")) mapType = IMap.MapType.GOOGLE;
        else if (type.equals("picture")) mapType = IMap.MapType.PICTURE;
        else throw new RuntimeException("Unsupported map type.");

        //metaData
        Map metaData = new HashMap<>();
        if(mapType == IMap.MapType.GOOGLE) {
            metaData.put("map", toml.getString("meta-data.map"));
        } else if(mapType == IMap.MapType.PICTURE) {
            metaData.put("map", toml.getString("meta-data.map"));
            metaData.put("dist_x", Double.toString(toml.getDouble("meta-data.dist_x")));
            metaData.put("dist_y", Double.toString(toml.getDouble("meta-data.dist_y")));
        }

        //dataSets
        List<Toml> dataToml = toml.getTables("data-set");
        ArrayList<DataSet> dataSets = new ArrayList<>(dataToml.size());

        for(int i=0; i<dataToml.size(); i++) {
            Toml cur = dataToml.get(i);
            String picture = cur.getString("picture");
            String description;
            if(cur.contains("description")) {
                description = cur.getString("description");
            } else {
                description = "";
            }
            
            Location location = null;
            if(mapType == IMap.MapType.GOOGLE) {
                location = new LocationGoogle(cur.getDouble("longitude"), cur.getDouble("latitude"));
            } else if(mapType == IMap.MapType.PICTURE) {
                location = new LocationPicture(cur.getLong("x").intValue(), cur.getLong("y").intValue());
            }

            dataSets.add(new DataSet(picture, description, location));
        }

        return new MapToml(name, mapType, metaData, dataSets);
    }

    public String getName() {
        return name;
    }

    public IMap.MapType getType() {
        return type;
    }

    public String getMetaData(String key) {
        return metaData.get(key);
    }

    public int getDataSetCount() {
        return dataSets.size();
    }

    public DataSet getDataSet(int id) {
        return dataSets.get(id);
    }

}
