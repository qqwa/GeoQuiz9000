package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import no.ntnu.tdt4240.geoquiz9000.R;

// TODO: 21.03.2017 call setResult() somewhere that returns distance between the target and the guess
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
                                                              GoogleMap.OnMapClickListener,
                                                              GoogleMap.OnMapLongClickListener
{
    public static Intent newIntent(Context context)
    {
        // TODO: 20.03.2017 add arguments (target's coordinates?)
        return new Intent(context, MapsActivity.class);
    }
    public static float getDistance(Intent i)
    {
        // TODO: 20.03.2017 extract distance from the intent passed to setResult(). To be called in onActivityResult() in the parent activity
        return 0f;
    }

    private static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Marker lastMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        Log.d(TAG, "Map is ready");
        mMap = googleMap;

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);

        LatLng startPoint = new LatLng(getResources().getInteger(R.integer.starting_point_lat),
                getResources().getInteger(R.integer.starting_point_long));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startPoint));
    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        Log.d(TAG, "Click on map");

        removeLastMarker();
    }

    @Override
    public void onMapLongClick(LatLng latLng)
    {
        Log.d(TAG, "Long click on map");

        removeLastMarker();

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        lastMarker = marker;
    }

    private void removeLastMarker()
    {
        if (lastMarker != null) {
            lastMarker.remove();
        }
    }

    public static float distanceBetweenTwoPoints(LatLng actualPoint, LatLng setPoint)
    {
        float[] results = new float[] { 0 };

        double actualLatitude = actualPoint.latitude;
        double actualLongitude = actualPoint.longitude;

        double eventLatitude = setPoint.latitude;
        double eventLongitude = setPoint.longitude;

        Location.distanceBetween(actualLatitude, actualLongitude, eventLatitude, eventLongitude, results);

        return results[0];
    }
}
