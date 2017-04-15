package no.ntnu.tdt4240.geoquiz9000.models;


import android.app.Activity;

import io.objectbox.Box;
import no.ntnu.tdt4240.geoquiz9000.database.DatabaseLayer;

public abstract class AbstractModel {

    /**
     * Generic method to save every ObjectBox model.
     *
     * @param activity Activity.
     */
    public void save(Activity activity) {
        DatabaseLayer layer = DatabaseLayer.getInstance(activity);
        Box box = layer.getBoxFor(this.getClass());
        box.put(this);
    }

}
