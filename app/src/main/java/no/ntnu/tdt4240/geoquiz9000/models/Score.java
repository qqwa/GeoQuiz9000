package no.ntnu.tdt4240.geoquiz9000.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Generated;
import io.objectbox.annotation.Id;
import io.objectbox.converter.PropertyConverter;

@Entity
public class Score extends AbstractModel implements Parcelable, Comparable<Score> {

    @Id
    private long id;

    private String playerName;
    private float totalDistance;

    @Convert(converter = ScoreConverter.class, dbType = String.class)
    private List<Float> distances;
    private String mapPackName;

    public Score(String playerName, float totalDistance, List<Float> distances, String mapPackName) {
        this.playerName = playerName;
        this.totalDistance = totalDistance;
        this.distances = distances;
        this.mapPackName = mapPackName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public float getTotalDistance() {
        return totalDistance;
    }

    public List<Float> getDistances() {
        return distances;
    }

    public String getMapPackName() {
        return mapPackName;
    }

    public void setTotalDistance(float totalDistance) {
        this.totalDistance = totalDistance;
    }

    public void setDistances(List<Float> distances) {
        this.distances = distances;
    }

    protected Score(Parcel in) {
        playerName = in.readString();
        totalDistance = in.readFloat();
        if (in.readByte() == 0x01) {
            distances = new ArrayList<Float>();
            in.readList(distances, Float.class.getClassLoader());
        } else {
            distances = null;
        }
        mapPackName = in.readString();
    }

    @Generated(hash = 689965711)
    public Score(long id, String playerName, float totalDistance, List<Float> distances,
            String mapPackName) {
        this.id = id;
        this.playerName = playerName;
        this.totalDistance = totalDistance;
        this.distances = distances;
        this.mapPackName = mapPackName;
    }

    @Generated(hash = 226049941)
    public Score() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(playerName);
        dest.writeFloat(totalDistance);
        if (distances == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(distances);
        }
        dest.writeString(mapPackName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Score> CREATOR = new Parcelable.Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };

    @Override
    public int compareTo(@NonNull Score score) {
        if (this.totalDistance == score.totalDistance) {
            return 0;

        } else {
            return this.totalDistance > score.totalDistance ? 1 : -1;
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setMapPackName(String mapPackName) {
        this.mapPackName = mapPackName;
    }

    public static class ScoreConverter implements PropertyConverter<List<Float>, String> {
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
            } catch (JSONException e) {
                Log.e(TAG, "Not possible to convert String to List<Float> again");
            }

            for (float dist: dists) {
                Log.d(TAG, String.valueOf(dist));
            }

            return dists;
        }

        @Override
        public String convertToDatabaseValue(List<Float> floats) {
            JSONArray jsonArray = new JSONArray(floats);
            Log.d(TAG, jsonArray.toString());
            return jsonArray.toString();
        }
    }
}