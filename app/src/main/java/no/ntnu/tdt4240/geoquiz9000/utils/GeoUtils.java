package no.ntnu.tdt4240.geoquiz9000.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.models.MapPicture;

public class GeoUtils
{
    public static float distanceBetweenTwoPoints(LatLng actualPoint, LatLng setPoint)
    {
        float[] results = new float[] { 0 };

        double actualLatitude = actualPoint.latitude;
        double actualLongitude = actualPoint.longitude;

        double eventLatitude = setPoint.latitude;
        double eventLongitude = setPoint.longitude;

        Location.distanceBetween(actualLatitude, actualLongitude, eventLatitude, eventLongitude,
                results);

        return results[0];
    }

    public static float distanceBetweenTwoPoints(MapPicture mapPicture, PointF answerPoint,
                                                 PointF setPoint)
    {
        float answerX = answerPoint.x;
        float answerY = answerPoint.y;

        float setX = setPoint.x;
        float setY = setPoint.y;

        return (float)Math.sqrt(Math.pow((setX - answerX) * mapPicture.getDistX(), 2) +
                Math.pow((setY - answerY) * mapPicture.getDistY(), 2));
    }

    public static float kmToMiles(float km)
    {
        return km * 0.621371192f;
    }
    public static float milesToKm(float miles)
    {
        return miles * 1.609344f;
    }

    public enum Units
    {
        KM("km"), MILES("miles");

        private String m_string;

        Units(String string)
        {
            m_string = string;
        }
        @Override
        public String toString()
        {
            return m_string;
        }
    }

    private static final String KEY_UNITS_IS_KM = "SharedPreferences.KEY_UNITS_IS_KM";

    public static Units getCurrentUnits(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(
                context.getString(R.string.preferences_file_name), Context.MODE_PRIVATE);
        boolean isKm = pref.getBoolean(KEY_UNITS_IS_KM, true);
        return isKm ? Units.KM : Units.MILES;
    }
    public static void setCurrentUnits(Context context, Units units)
    {
        SharedPreferences pref = context.getSharedPreferences(
                context.getString(R.string.preferences_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor writer = pref.edit();
        writer.putBoolean(KEY_UNITS_IS_KM, units == Units.KM);
        writer.commit();
    }
}
