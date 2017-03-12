package no.ntnu.tdt4240.geoquiz9000.database;

import android.app.Activity;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import no.ntnu.tdt4240.geoquiz9000.App;

public class DatabaseLayer {

    private static DatabaseLayer instance = null;
    private BoxStore mBoxStore;

    protected DatabaseLayer(Activity activity) {
        App app = (App) activity.getApplication();
        mBoxStore = app.getBoxStore();
    }

    public static DatabaseLayer getInstance(Activity activity) {
        if (instance == null) {
            instance = new DatabaseLayer(activity);
        }

        return instance;
    }

    public BoxStore getBoxStore() {
        return mBoxStore;
    }

    public Box getBoxFor(Class c) {
        return mBoxStore.boxFor(c);
    }

    public List getAll(Class c) {
        Box box = getBoxFor(c);
        return box.getAll();
    }
}
