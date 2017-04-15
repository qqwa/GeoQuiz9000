package no.ntnu.tdt4240.geoquiz9000.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Score extends AbstractModel implements Parcelable {

    private String playerName;
    private float totalDistance;
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
        totalDistance = in.readFloat();
        if (in.readByte() == 0x01) {
            distances = new ArrayList<Float>();
            in.readList(distances, Float.class.getClassLoader());
        } else {
            distances = null;
        }
        mapPackName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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
}