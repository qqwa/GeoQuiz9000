package no.ntnu.tdt4240.geoquiz9000.controllers;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.greenrobot.essentials.io.IoUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import io.objectbox.Box;
import no.ntnu.tdt4240.geoquiz9000.database.DatabaseLayer;
import no.ntnu.tdt4240.geoquiz9000.models.IMap;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;
import no.ntnu.tdt4240.geoquiz9000.models.MapToml;

public class AsyncImportMap extends AsyncTask<Void, String, MapStore> {
    private InputStream mInputStream;
    private Activity mActivity;
    private String mUrl;
    private String mTmpPath;
    private String mError = "";
    public AsyncImportMap(InputStream inputStream, Activity activity) {
        this.mInputStream = inputStream;
        this.mActivity = activity;
        this.mUrl = null;
        mTmpPath = activity.getFilesDir().getPath() + "/tmp/";
    }

    public AsyncImportMap(String url, Activity activity) {
        this.mInputStream = null;
        this.mActivity = activity;
        this.mUrl = url;
        mTmpPath = activity.getFilesDir().getPath() + "/tmp/";
    }

    public String getErrorMessage() {
        return mError;
    }

    @Override
    protected MapStore doInBackground(Void... params) {
        try {
            if(mInputStream == null) { //download map
                publishProgress("Downloading map...");
                URL mapPack = new URL(mUrl);
                mInputStream = mapPack.openStream();
            }
            if(isCancelled()) {
                return null;
            }
            publishProgress("Unzipping archive...");

            String root = "";

            //read zip file
            ZipInputStream input = new ZipInputStream(mInputStream);
            ZipEntry curEntry = null;
            boolean firstRun = true;
            while((curEntry = input.getNextEntry()) != null) {
                if(firstRun && curEntry.isDirectory()) {
                    root = curEntry.getName();
                }
                firstRun = false;

                if(!curEntry.isDirectory()) {
                    writeFile(mActivity, mTmpPath + curEntry.getName(), input);
                } else {
                    File file = new File(mTmpPath + curEntry.getName());
                    if(!file.exists()) {
                        file.mkdirs();
                    }
                }

                input.closeEntry();
                if(isCancelled()) {
                    input.close();
                    cleanUp();
                    return null;
                }
            }

            input.close();

            if(isCancelled()) {
                cleanUp();
                return null;
            }
            publishProgress("Validating Map Pack...");

            MapToml mapToml = mapToml = MapToml.readToml(new FileInputStream(new File(mTmpPath + root + "map.toml")));
            if(mapToml == null) {
                mError = "Error: Map Pack doesn't contain a map.toml file!";
                cleanUp();
                return null;
            }
            //check if mapPack exists already
            Box mapStores = DatabaseLayer.getInstance(mActivity).getBoxFor(MapStore.class);
            if(mapStores.find("rootPath", mActivity.getFilesDir().getPath() + "/" + mapToml.getRootPath()).size() != 0) {
                //error map already imported
                mError = "Error: Map Pack already imported!";
                cleanUp();
                return null;
            }

            if(isCancelled()) {
                cleanUp();
                return null;
            }
            publishProgress("Importing Map...");
//            File newFolder = new File(mActivity.getFilesDir().getPath() + "/" + mapToml.getRootPath());
//            newFolder.mkdirs();
            File tmpFolder = new File(mTmpPath + "/" + root);
            tmpFolder.renameTo(new File(mActivity.getFilesDir().getPath() + "/" + mapToml.getRootPath()));

            MapStore result = new MapStore(mapToml.getName(), IMap.MapType.convert(mapToml.getType()),
                    mActivity.getFilesDir().getPath() + "/" + mapToml.getRootPath(), mapToml.getDataSetCount());
            result.save(mActivity);


            publishProgress("Finishing...");
            Log.i("IMPORT", "Imported " + result.getName());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            mError = "Error: Unexpected error occurred.";
            cleanUp();
        }
        return null;
    }

    private void cleanUp() {
        if(mError.equals("")) {
            mError = "Canceling...";
        }
        File tmpFolder = new File(mTmpPath);
        if(tmpFolder.exists()) {
            deleteFolder(tmpFolder);
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
        if(file != null) {
            if(file.isDirectory() && file.listFiles() != null) {
                for(File f : file.listFiles()) {
                    deleteFolder(f);
                }
            }
            file.delete();
        }
    }
}
