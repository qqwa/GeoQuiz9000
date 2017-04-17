package no.ntnu.tdt4240.geoquiz9000.controllers;


import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import no.ntnu.tdt4240.geoquiz9000.models.MapStore;
import no.ntnu.tdt4240.geoquiz9000.models.MapToml;

public class AsyncAddQuestion extends AsyncTask<Void, String, MapStore> {
    private MapStore mMap;
    private Bitmap mPicture;
    private MapToml.Location mLocation;

    public AsyncAddQuestion(MapStore map, Bitmap picture, MapToml.Location location) {
        this.mMap = map;
        this.mPicture = picture;
        this.mLocation = location;
    }

    @Override
    protected MapStore doInBackground(Void... params) {
        MapToml toml = null;
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            mPicture.compress(Bitmap.CompressFormat.JPEG, 10, stream);
            MessageDigest md = null;
            md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(stream.toByteArray());
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for(byte b: hash) {
                sb.append(String.format("%02x", b));
            }

            File tomlFile = new File(mMap.getRootPath(), "map.toml");
            File pictureFile = new File(mMap.getRootPath(), sb.toString() + ".jpg");
            toml = MapToml.readToml(new FileInputStream(new File(mMap.getRootPath(), "map.toml")));
            toml.add(pictureFile.getName(), new MapToml.LocationPicture(50, 50));
            toml.write(new FileOutputStream(tomlFile));
            //write picture
            mPicture.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(pictureFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}
