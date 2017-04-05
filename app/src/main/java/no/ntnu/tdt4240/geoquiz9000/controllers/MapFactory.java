package no.ntnu.tdt4240.geoquiz9000.controllers;


import android.app.Activity;

import org.greenrobot.essentials.io.IoUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import no.ntnu.tdt4240.geoquiz9000.models.IMap;
import no.ntnu.tdt4240.geoquiz9000.models.MapGoogle;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;
import no.ntnu.tdt4240.geoquiz9000.models.MapToml;

public final class MapFactory {

    //Activity which calls this method has to save the returned MapStore
    public static MapStore importMap(InputStream mapZip, Activity activity) throws Exception {
        //delete tmp folder, just to be sure
        String tmpPath = activity.getFilesDir().getPath() + "/tmp/";
        String root = "";

        File tmpFolder = new File(tmpPath);
        if(tmpFolder.exists()) {
            deleteFolder(tmpFolder);
        }

        //read zip file
        ZipInputStream input = new ZipInputStream(mapZip);
        ZipEntry curEntry = null;
        boolean firstRun = true;
        while((curEntry = input.getNextEntry()) != null) {
            if(firstRun && curEntry.isDirectory()) {
                root = curEntry.getName();
            }
            firstRun = false;

            if(!curEntry.isDirectory()) {
                writeFile(activity, tmpPath + curEntry.getName(), input);
            } else {
                File file = new File(tmpPath + curEntry.getName());
                if(!file.exists()) {
                    file.mkdirs();
                }
            }

            input.closeEntry();
        }

        input.close();

        MapToml mapToml = mapToml = MapToml.readToml(new FileInputStream(new File(tmpPath + root + "map.toml")));
        if(mapToml == null) {
            throw new FileNotFoundException("Couldn't find map.toml");
        }

        //move files to permanent location
        File newFolder = new File(activity.getFilesDir().getPath() + "/" + mapToml.getRootPath());
        newFolder.mkdirs();
        tmpFolder = new File(tmpPath + "/" + root);
        tmpFolder.renameTo(new File(activity.getFilesDir().getPath() + "/" + mapToml.getRootPath()));

        deleteFolder(new File(tmpPath));
        return new MapStore(mapToml.getName(), IMap.MapType.convert(mapToml.getType()),
                activity.getFilesDir().getPath() + "/" + mapToml.getRootPath(), mapToml.getDataSetCount());
    }

    public static IMap getMap(IMap mapStore) throws FileNotFoundException {
        if(mapStore.getType() == IMap.MapType.GOOGLE) {
            File tomlFile = new File(mapStore.getRootPath() + "/map.toml");
            MapToml mapToml = MapToml.readToml(new FileInputStream(tomlFile));
            ArrayList<String> pictures = new ArrayList<>();
            ArrayList<String> description = new ArrayList<>();
            ArrayList<Double> locationsLatitude = new ArrayList<>();
            ArrayList<Double> locationsLongitude = new ArrayList<>();

            for(MapToml.DataSet dataSet : mapToml.getDataSets()) {
                pictures.add(dataSet.picture);
                description.add(dataSet.description);
                locationsLatitude.add(((MapToml.LocationGoogle)dataSet.location).latitude);
                locationsLongitude.add(((MapToml.LocationGoogle)dataSet.location).longitude);
            }

            return new MapGoogle(mapToml, mapStore.getRootPath(), pictures, description,
                    locationsLatitude, locationsLongitude);
        } else if (mapStore.getType() == IMap.MapType.PICTURE) {
            //TODO: load picture maps
            return null;
        } else {
            return null;
        }
    }

    private static void writeFile(Activity activity, String path, InputStream inputStream) throws IOException {
        File file = new File(path);
        file.createNewFile();
        OutputStream fileOutputStream = new FileOutputStream(file);
        IoUtils.copyAllBytes(inputStream, fileOutputStream);
        fileOutputStream.close();
    }

    private static void deleteFolder(File file) {
        if(file != null && file.listFiles() != null) {
            for(File f : file.listFiles()) {
                deleteFolder(f);
            }
            file.delete();
        }
    }


}
