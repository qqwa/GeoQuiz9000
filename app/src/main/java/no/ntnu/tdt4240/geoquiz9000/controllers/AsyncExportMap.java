package no.ntnu.tdt4240.geoquiz9000.controllers;

import android.os.AsyncTask;

import org.greenrobot.essentials.io.IoUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import no.ntnu.tdt4240.geoquiz9000.models.MapStore;

public class AsyncExportMap extends AsyncTask<Void, String, Void> {
    private MapStore mMap;
    private File mFile;

    public AsyncExportMap(MapStore map, File file) {
        this.mMap = map;
        this.mFile = file;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            if (!mFile.createNewFile()) {
                publishProgress("Error: Couldn't create File!");
                return null;
            }
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(mFile));
            String rootFolderName = mMap.getName().replace(" ", "");
            File folder = new File(mMap.getRootPath());

            zipFolder(folder, zipOutputStream, rootFolderName);
            zipOutputStream.close();

            publishProgress("Finished...");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            publishProgress("Error: File not found!");
        }
        catch (IOException e) {
            e.printStackTrace();
            publishProgress("Error: IO Exception!");
        }

        return null;
    }

    private void zipFolder(File folder, ZipOutputStream zipOutputStream, String rootFolderName) throws IOException {
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                ZipEntry entry = new ZipEntry(rootFolderName + "/" + file.getPath().replace(mMap.getRootPath() + "/", ""));
                zipOutputStream.putNextEntry(entry);
                InputStream inputStream = new FileInputStream(file);
                IoUtils.copyAllBytes(inputStream, zipOutputStream);
                zipOutputStream.closeEntry();
            }
            else if (file.isDirectory()) {
                zipFolder(file, zipOutputStream, rootFolderName);
            }
        }

    }
}
