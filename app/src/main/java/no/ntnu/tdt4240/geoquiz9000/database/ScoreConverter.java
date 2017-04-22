package no.ntnu.tdt4240.geoquiz9000.database;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.converter.PropertyConverter;

/**
 * Class for converting float list to String to make it compatible with ObjectBox.
 */
public class ScoreConverter implements PropertyConverter<List<Float>, String> {
    private static final String TAG = ScoreConverter.class.getSimpleName();

    @Override
    public List<Float> convertToEntityProperty(String s) {
        List<Float> dists = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                String jsonValue = jsonArray.opt(i).toString();
                dists.add(Float.valueOf(jsonValue));
            }
        }
        catch (JSONException e) {
            Log.e(TAG, "Not possible to convert String to List<Float> again");
        }

        return dists;
    }

    @Override
    public String convertToDatabaseValue(List<Float> floats) {
        JSONArray jsonArray = new JSONArray(floats);
        return jsonArray.toString();
    }
}
