package no.ntnu.tdt4240.geoquiz9000;

import android.app.Application;
import android.os.AsyncTask;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import no.ntnu.tdt4240.geoquiz9000.controllers.AsyncImportMap;
import no.ntnu.tdt4240.geoquiz9000.controllers.MapFactory;
import no.ntnu.tdt4240.geoquiz9000.database.DatabaseLayer;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;
import no.ntnu.tdt4240.geoquiz9000.models.MyObjectBox;

public class App extends Application {

    private BoxStore mBoxStore;

    @Override
    public void onCreate() {
        super.onCreate();

        mBoxStore = MyObjectBox.builder().androidContext(App.this).build();
    }

    public BoxStore getBoxStore() {
        return mBoxStore;
    }

}
