package no.ntnu.tdt4240.geoquiz9000;

import android.app.Application;

import io.objectbox.BoxStore;
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
