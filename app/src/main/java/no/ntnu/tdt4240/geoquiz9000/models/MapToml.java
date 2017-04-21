package no.ntnu.tdt4240.geoquiz9000.models;


import android.util.Log;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapToml {
    public static class DataSet {
        public String picture;
        public String description;
        public IMap.Location location;

        public DataSet(String picture, String description, IMap.Location location) {
            this.picture = picture;
            this.description = description;
            this.location = location;
        }
    }
    private String name;
    private IMap.MapType type;
    public String rootPath;
    private Map<String, String> metaData;
    private ArrayList<DataSet> dataSets;

    public MapToml(String name, IMap.MapType type, Map metaData, ArrayList<DataSet> dataSets) {
        this.name = name;
        this.type = type;
        this.metaData = metaData;
        this.dataSets = dataSets;

        String hashData = name;
        for(DataSet dataSet : dataSets) {
            name += dataSet.picture + dataSet.description;
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(hashData.getBytes());
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for(byte b: hash) {
                sb.append(String.format("%02x", b));
            }
            rootPath = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        rootPath += this.name.replace(" ", "");

    }

    static public MapToml readToml(InputStream inputStream) {
        Toml toml = new Toml().read(inputStream);

        if(toml == null) {
            return null;
        }

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
        Map<String, String> metaData = new HashMap<>();
        if(mapType == IMap.MapType.GOOGLE) {
            metaData.put("map", toml.getString("meta_data.map"));
        } else if(mapType == IMap.MapType.PICTURE) {
            metaData.put("map", toml.getString("meta_data.map"));
            metaData.put("dist_x", Double.toString(toml.getDouble("meta_data.dist_x")));
            metaData.put("dist_y", Double.toString(toml.getDouble("meta_data.dist_y")));
        }

        //dataSets
        List<Toml> dataToml = toml.getTables("data_set");
        ArrayList<DataSet> dataSets = new ArrayList<>(dataToml.size());

        for (int i = 0; i < dataToml.size(); i++) {
            Toml cur = dataToml.get(i);
            String picture = cur.getString("picture");
            String description;
            if (cur.contains("description")) {
                description = cur.getString("description");
            }
            else {
                description = "";
            }

            IMap.Location location = null;
            if (mapType == IMap.MapType.GOOGLE) {
                location = new MapGoogle.Location(cur.getDouble("longitude"), cur.getDouble("latitude"));
            }
            else if (mapType == IMap.MapType.PICTURE) {
                location = new MapPicture.Location(cur.getLong("x").intValue(), cur.getLong("y").intValue());
            }

            dataSets.add(new DataSet(picture, description, location));
        }

        return new MapToml(name, mapType, metaData, dataSets);
    }

    public static class TomlRep {
        String name;
        String type;
        Map<String, Object> meta_data;
        ArrayList<Map<String, Object>> data_set;
    }

    public void write(OutputStream outputStream) {
        TomlWriter writer = new TomlWriter();
        TomlRep tomlRep = new TomlRep();

        //toplevel data
        tomlRep.name = name;
        tomlRep.type = type == IMap.MapType.GOOGLE ? "google-maps" : "picture";

        //meta_data
        tomlRep.meta_data = new HashMap<>();
        tomlRep.meta_data.put("map", metaData.get("map"));
        if(type == IMap.MapType.PICTURE) {
            tomlRep.meta_data.put("dist_x", metaData.get("dist_x"));
            tomlRep.meta_data.put("dist_y", metaData.get("dist_y"));
        }

        //data_set
        tomlRep.data_set = new ArrayList<>();
        for(DataSet dataSet : dataSets) {
            Map<String, Object> data = new HashMap<>();
            data.put("picture", dataSet.picture);
            data.put("description", dataSet.description);
            if(type == IMap.MapType.GOOGLE) {
                MapGoogle.Location locationGoogle = (MapGoogle.Location)dataSet.location;
                data.put("latitude", locationGoogle.latitude);
                data.put("longitude", locationGoogle.longitude);
            } else {
                MapPicture.Location locationPicture = (MapPicture.Location)dataSet.location;
                data.put("x", locationPicture.x);
                data.put("y", locationPicture.y);
            }
            tomlRep.data_set.add(data);
        }

        try {
            writer.write(tomlRep, outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(String picturePath, IMap.Location location) {
        add(picturePath, location, null);
    }

    public void add(String picturePath, IMap.Location location, String description) {
        if(description == null) {
            description = "";
        }
        if(location instanceof MapGoogle.Location && type != IMap.MapType.GOOGLE
                || location instanceof MapPicture.Location && type != IMap.MapType.PICTURE) {
            Log.e("MapToml", "Tried to add new Question with wrong type of location");
            return;
        }
        dataSets.add(new DataSet(picturePath, description, location));
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

    public ArrayList<DataSet> getDataSets() { return dataSets; }

    public String getRootPath() { return rootPath; }

}
