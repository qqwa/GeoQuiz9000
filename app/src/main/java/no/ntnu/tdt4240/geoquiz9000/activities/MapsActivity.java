package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.controllers.MapFactory;
import no.ntnu.tdt4240.geoquiz9000.database.DatabaseLayer;
import no.ntnu.tdt4240.geoquiz9000.models.MapGoogle;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;
import no.ntnu.tdt4240.geoquiz9000.models.Score;
import no.ntnu.tdt4240.geoquiz9000.ui.PictureDialogFragment;
import no.ntnu.tdt4240.geoquiz9000.utils.GeoUtils;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener {

    private static final String TAG = MapsActivity.class.getSimpleName();
    public static final String PIC_DIALOG = "picture dialog";
    public static final String INTENT_PACKAGE_NAME = "intent package name";
    public static final String INTENT_NR_OF_PLAYERS = "intent nr of players";

    private String mMapName;
    private GoogleMap mMap;
    private Marker lastMarker;
    private MapGoogle mapGoogle;
    private MapStore testMap;
    private int mCurrentQuestionNr;
    private float mCurrentCalculatedDistance;
    private FloatingActionButton mActionButton;
    private TextView mPlayerTv;
    private List<Float> mDistances = new ArrayList<>();
    private int mNrOfPlayers;
    private String mCurrentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mMapName = getIntent().getStringExtra(INTENT_PACKAGE_NAME);
        mNrOfPlayers = getIntent().getIntExtra(INTENT_NR_OF_PLAYERS, 1);

        mActionButton = (FloatingActionButton) findViewById(R.id.action_button);
        mActionButton.setOnClickListener(onActionButtonClick);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        loadMapPackage();
        if (testMap != null && mapGoogle != null) {
            mCurrentQuestionNr = 0;
            initializeQuestion(0);
        }

        mPlayerTv = (TextView) findViewById(R.id.player_id);
        if (mNrOfPlayers == 1) {
            mCurrentPlayer = getString(R.string.player) + " 1";
            mPlayerTv.setText(mCurrentPlayer);
        }
    }

    public static Intent newIntent(Context context, String mapPackageName, int nrOfPlayers) {
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra(INTENT_PACKAGE_NAME, mapPackageName);
        intent.putExtra(INTENT_NR_OF_PLAYERS, nrOfPlayers);
        return intent;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Map is ready");
        mMap = googleMap;

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);

        LatLng startPoint = new LatLng(getResources().getInteger(R.integer.starting_point_lat),
                getResources().getInteger(R.integer.starting_point_long));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startPoint));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d(TAG, "Click on map");

        removeLastMarker();
    }

    @Override
    public void onMapLongClick(LatLng setPoint) {
        Log.d(TAG, "Long click on map");

        removeLastMarker();

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(setPoint)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        lastMarker = marker;

        LatLng answerPoint = new LatLng(mapGoogle.getLocationLatitude(mCurrentQuestionNr),
                mapGoogle.getLocationLongitude(mCurrentQuestionNr));

        float calculatedDistance = GeoUtils.distanceBetweenTwoPoints(setPoint, answerPoint);
        Log.d(TAG, "Distance in meters: " + String.valueOf(calculatedDistance));
        mCurrentCalculatedDistance = calculatedDistance;

        mActionButton.setVisibility(View.VISIBLE);
        if (isLastQuestion()) {
            mActionButton.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ic_done_white_24px));
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.quit_game))
                .setMessage(getString(R.string.sure_to_quit_game))
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MapsActivity.super.onBackPressed();
                    }
                })
                .create();
        dialog.show();
    }

    private void removeLastMarker() {
        mActionButton.setVisibility(View.GONE);
        mCurrentCalculatedDistance = 0;

        if (lastMarker != null) {
            lastMarker.remove();
        }
    }

    private void loadMapPackage() {
        Box maps = DatabaseLayer.getInstance(this).getBoxFor(MapStore.class);

        try {
            //load imported map
            if (maps.find("name", mMapName).size() != 0) {
                testMap = (MapStore) maps.find("name", mMapName).get(0);
                mapGoogle = (MapGoogle) MapFactory.getMap(testMap);
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not load map package.");
            Toast.makeText(this, getString(R.string.error_load_map_package), Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Initialize the question
     * @param questionNr nr of question
     */
    private void initializeQuestion(final int questionNr) {
        final String picPath = testMap.getRootPath() + "/" + mapGoogle.getLocationPicturePath(questionNr);

        showDialog(questionNr, picPath);

        ImageView questionPic = (ImageView) findViewById(R.id.image_preview);
        Bitmap pic = BitmapFactory.decodeFile(picPath);
        questionPic.setImageBitmap(pic);
        questionPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(questionNr, picPath);
            }
        });
    }

    /**
     * Show question and picture again
     * @param questionNr nr of question
     * @param picPath path to picture
     */
    private void showDialog(int questionNr, String picPath) {
        Bundle bundle = new Bundle();
        bundle.putInt(PictureDialogFragment.NR_OF_QUESTION, questionNr);
        bundle.putString(PictureDialogFragment.IMAGE_PATH, picPath);

        PictureDialogFragment dialog = new PictureDialogFragment();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), PIC_DIALOG);
    }

    private void nextQuestion() {
        mDistances.add(mCurrentCalculatedDistance);

        removeLastMarker();

        mCurrentQuestionNr++;
        initializeQuestion(mCurrentQuestionNr);
    }

    View.OnClickListener onActionButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int maxNrOfQuestions = mapGoogle.getLocationCount();
            if (mCurrentQuestionNr < maxNrOfQuestions - 1) {
                nextQuestion();
            } else {
                float result = 0;
                for (float distance : mDistances) {
                    result = result + distance;
                }
                Log.d(TAG, "In total: " + String.valueOf(result));

                Score score = new Score(result, mDistances, mMapName);
                Intent intent = ResultActivity.newIntent(getApplicationContext(), score);
                startActivity(intent);
                finish();
            }
        }
    };

    private boolean isLastQuestion() {
        boolean isLastQuestion = false;
        int maxNrOfQuestions = mapGoogle.getLocationCount();

        if (mCurrentQuestionNr == maxNrOfQuestions - 1) {
            isLastQuestion = true;
        }

        return isLastQuestion;
    }
}
