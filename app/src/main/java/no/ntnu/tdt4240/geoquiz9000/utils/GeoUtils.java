package no.ntnu.tdt4240.geoquiz9000.utils;

import android.graphics.PointF;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import no.ntnu.tdt4240.geoquiz9000.models.MapPicture;

public class GeoUtils {
    public static float distanceBetweenTwoPoints(LatLng actualPoint, LatLng setPoint) {
        float[] results = new float[]{0};

        double actualLatitude = actualPoint.latitude;
        double actualLongitude = actualPoint.longitude;

        double eventLatitude = setPoint.latitude;
        double eventLongitude = setPoint.longitude;

        Location.distanceBetween(actualLatitude, actualLongitude, eventLatitude, eventLongitude,
                results);

        return results[0];
    }

    public static float distanceBetweenTwoPoints(MapPicture mapPicture, PointF answerPoint,
                                                 PointF setPoint) {
        float answerX = answerPoint.x;
        float answerY = answerPoint.y;

        float setX = setPoint.x;
        float setY = setPoint.y;

        return (float) Math.sqrt(Math.pow((setX - answerX) * mapPicture.getDistX(), 2) +
                Math.pow((setY - answerY) * mapPicture.getDistY(), 2));
    }
}
