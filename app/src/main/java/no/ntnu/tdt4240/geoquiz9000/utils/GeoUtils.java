package no.ntnu.tdt4240.geoquiz9000.utils;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class GeoUtils {
    public static float distanceBetweenTwoPoints(LatLng actualPoint, LatLng setPoint) {
        float[] results = new float[]{0};

        double actualLatitude = actualPoint.latitude;
        double actualLongitude = actualPoint.longitude;

        double eventLatitude = setPoint.latitude;
        double eventLongitude = setPoint.longitude;

        Location.distanceBetween(actualLatitude, actualLongitude, eventLatitude, eventLongitude, results);

        return results[0];
    }
}
