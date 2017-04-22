package no.ntnu.tdt4240.geoquiz9000.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import no.ntnu.tdt4240.geoquiz9000.models.IMap;
import no.ntnu.tdt4240.geoquiz9000.models.MapGoogle;
import no.ntnu.tdt4240.geoquiz9000.models.MapPicture;
import no.ntnu.tdt4240.geoquiz9000.models.MapToml;

public final class MapFactory {

    public static IMap getMap(IMap mapStore) throws FileNotFoundException {
        if (mapStore.getType() == IMap.MapType.GOOGLE) {
            File tomlFile = new File(mapStore.getRootPath() + "/map.toml");
            MapToml mapToml = MapToml.readToml(new FileInputStream(tomlFile));
            ArrayList<String> pictures = new ArrayList<>();
            ArrayList<String> description = new ArrayList<>();
            ArrayList<Double> locationsLatitude = new ArrayList<>();
            ArrayList<Double> locationsLongitude = new ArrayList<>();

            for (MapToml.DataSet dataSet : mapToml.getDataSets()) {
                pictures.add(dataSet.picture);
                description.add(dataSet.description);
                locationsLatitude.add(((MapGoogle.Location)dataSet.location).latitude);
                locationsLongitude.add(((MapGoogle.Location)dataSet.location).longitude);
            }

            return new MapGoogle(mapToml, mapStore.getRootPath(), pictures, description,
                    locationsLatitude, locationsLongitude);
        }
        else if (mapStore.getType() == IMap.MapType.PICTURE) {
            File tomlFile = new File(mapStore.getRootPath() + "/map.toml");
            MapToml mapToml = MapToml.readToml(new FileInputStream(tomlFile));
            ArrayList<String> pictures = new ArrayList<>();
            ArrayList<String> description = new ArrayList<>();
            ArrayList<Integer> locationsX = new ArrayList<>();
            ArrayList<Integer> locationsY = new ArrayList<>();

            for (MapToml.DataSet dataSet : mapToml.getDataSets()) {
                pictures.add(dataSet.picture);
                description.add(dataSet.description);
                locationsX.add(((MapPicture.Location)dataSet.location).x);
                locationsY.add(((MapPicture.Location)dataSet.location).y);
            }

            return new MapPicture(mapToml, mapStore.getRootPath(), pictures, description,
                    locationsX, locationsY);
        }
        else {
            return null;
        }
    }

}
