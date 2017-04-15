package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.objectbox.Box;
import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.controllers.MapFactory;
import no.ntnu.tdt4240.geoquiz9000.database.DatabaseLayer;
import no.ntnu.tdt4240.geoquiz9000.dialogs.PictureDialogFragment;
import no.ntnu.tdt4240.geoquiz9000.dialogs.ResultDialog;
import no.ntnu.tdt4240.geoquiz9000.models.MapGoogle;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;
import no.ntnu.tdt4240.geoquiz9000.models.Score;
import no.ntnu.tdt4240.geoquiz9000.utils.GeoUtils;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener {

    private static final String TAG = MapsActivity.class.getSimpleName();

    public static final String PIC_DIALOG = "picture dialog";
    public static final String RESULT_DIALOG = "result dialog";
    public static final String INTENT_PACKAGE_NAME = "intent package name";
    public static final String INTENT_NR_OF_PLAYERS = "intent nr of players";

    private String mMapName;
    private GoogleMap mMap;
    private Marker lastMarker;
    private MapGoogle mapGoogle;
    private MapStore mapStore;
    private int mCurrentQuestionNr;
    private float mCurrentCalculatedDistance;
    private FloatingActionButton mActionButton;
    private TextView mPlayerTv;
    private Map<Integer, Score> mScores = new HashMap<>();
    private Map<String, LatLng> mPoints = new HashMap<>();
    private LatLng mCurrentPoint;
    private int mNrOfPlayers;
    private String mCurrentPlayer;
    private int mCurrentPlayerNr;
    private boolean mShowAnswer;

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

        mPlayerTv = (TextView) findViewById(R.id.player_id);
        mCurrentPlayerNr = 1;
        setPlayerName(mCurrentPlayerNr);

        loadMapPackage();
        if (mapStore != null && mapGoogle != null) {
            mCurrentQuestionNr = 0;
            initializeQuestion(0);
        }
    }

    /**
     * Initialize the Intent.
     *
     * @param context Context
     * @param mapPackageName name of game package
     * @param nrOfPlayers number of players
     * @return Intent
     */
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
        if (!mShowAnswer) {
            mActionButton.setVisibility(View.GONE);
        }

        removeLastMarker();
    }

    @Override
    public void onMapLongClick(LatLng setPoint) {
        Log.d(TAG, "Long click on map");

        if (!mShowAnswer) {
            mActionButton.setVisibility(View.GONE);

            removeLastMarker();

            mCurrentPoint = setPoint;

            lastMarker = mMap.addMarker(new MarkerOptions()
                    .position(setPoint)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            LatLng answerPoint = new LatLng(mapGoogle.getLocationLatitude(mCurrentQuestionNr),
                    mapGoogle.getLocationLongitude(mCurrentQuestionNr));

            float calculatedDistance = GeoUtils.distanceBetweenTwoPoints(setPoint, answerPoint);
            Log.d(TAG, "Distance in meters: " + String.valueOf(calculatedDistance));
            mCurrentCalculatedDistance = calculatedDistance;

            mActionButton.setVisibility(View.VISIBLE);
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

    /**
     * Remove the last marker.
     */
    private void removeLastMarker() {
        mCurrentCalculatedDistance = 0;
        mCurrentPoint = null;

        if (lastMarker != null) {
            lastMarker.remove();
        }
    }

    /**
     * Load map package.
     */
    private void loadMapPackage() {
        Box maps = DatabaseLayer.getInstance(this).getBoxFor(MapStore.class);

        try {
            if (maps.find("name", mMapName).size() != 0) {
                mapStore = (MapStore) maps.find("name", mMapName).get(0);
                mapGoogle = (MapGoogle) MapFactory.getMap(mapStore);
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not load map package.");
            Toast.makeText(this, getString(R.string.error_load_map_package), Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Initialize the question
     *
     * @param questionNr nr of question
     */
    private void initializeQuestion(final int questionNr) {
        final String picPath = mapStore.getRootPath() + "/" + mapGoogle.getLocationPicturePath(questionNr);

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
     *
     * @param questionNr nr of question
     * @param picPath    path to picture
     */
    private void showDialog(int questionNr, String picPath) {
        Bundle bundle = new Bundle();
        bundle.putInt(PictureDialogFragment.NR_OF_QUESTION, questionNr);
        bundle.putString(PictureDialogFragment.IMAGE_PATH, picPath);
        bundle.putString(PictureDialogFragment.PLAYER_NAME, mCurrentPlayer);

        PictureDialogFragment dialog = new PictureDialogFragment();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), PIC_DIALOG);
    }

    /**
     * Load next question.
     */
    private void nextQuestion() {
        mMap.clear();
        removeLastMarker();

        mCurrentQuestionNr++;
        initializeQuestion(mCurrentQuestionNr);
    }

    View.OnClickListener onNextQuestionClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mShowAnswer = false;

            int maxNrOfQuestions = mapGoogle.getLocationCount();
            boolean notLastQuestion = mCurrentQuestionNr < maxNrOfQuestions - 1;
            if (notLastQuestion) {
                nextQuestion();

                setPlayerName(mCurrentPlayerNr);

                mActionButton.setVisibility(View.GONE);
                mActionButton.setOnClickListener(onActionButtonClick);
            } else {
                ArrayList<Score> finalScores = new ArrayList<>();

                for (int i = 1; i <= mNrOfPlayers; i++) {
                    Score finalScore = mScores.get(i);
                    List<Float> finalDistances = finalScore.getDistances();

                    float result = 0;
                    for (float distance : finalDistances) {
                        result = result + distance;
                    }
                    Log.d(TAG, "In total: " + String.valueOf(result));
                    finalScore.setTotalDistance(result);

                    finalScores.add(finalScore);
                }

                // Finish game.
                Intent intent = ResultActivity.newIntent(getApplicationContext(), finalScores);
                startActivity(intent);
                finish();
            }
        }
    };

    View.OnClickListener onActionButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int maxNrOfQuestions = mapGoogle.getLocationCount();

            mPoints.put(mCurrentPlayer, mCurrentPoint);

            // Get Score object
            Score score = mScores.get(mCurrentPlayerNr);
            if (score == null) {
                score = initializePlayer(mCurrentPlayerNr, mCurrentPlayer);
            }

            // Add new values to Score object
            List<Float> distances = score.getDistances();
            distances.add(mCurrentCalculatedDistance);
            score.setDistances(distances);
            mScores.put(mCurrentPlayerNr, score);

            if (mCurrentQuestionNr < maxNrOfQuestions - 1) {
                nextPlayer(mCurrentPlayerNr);

                if (mCurrentPlayerNr == 1) {
                    removeLastMarker();

                    LatLng answerPoint = new LatLng(mapGoogle.getLocationLatitude(mCurrentQuestionNr),
                            mapGoogle.getLocationLongitude(mCurrentQuestionNr));
                    showResult(answerPoint);

                    mActionButton.setVisibility(View.VISIBLE);
                    mActionButton.setOnClickListener(onNextQuestionClick);
                } else {
                    removeLastMarker();

                    mActionButton.setVisibility(View.GONE);
                    initializeQuestion(mCurrentQuestionNr);
                }
            } else {
                nextPlayer(mCurrentPlayerNr);

                if (mCurrentPlayerNr != 1) {
                    removeLastMarker();
                    initializeQuestion(mCurrentQuestionNr);
                } else {
                    removeLastMarker();

                    LatLng answerPoint = new LatLng(mapGoogle.getLocationLatitude(mCurrentQuestionNr),
                            mapGoogle.getLocationLongitude(mCurrentQuestionNr));
                    showResult(answerPoint);

                    mActionButton.setVisibility(View.VISIBLE);
                    mActionButton.setOnClickListener(onNextQuestionClick);
                }
            }
        }
    };

    /**
     * Set the player name in the box.
     * @param nr player number
     */
    private void setPlayerName(int nr) {
        String playerNumber = String.valueOf(nr);
        mCurrentPlayer = getString(R.string.player) + " " + playerNumber;
        mPlayerTv.setText(mCurrentPlayer);
    }

    /**
     * Set up next player.
     * @param currentNr current number of player
     */
    private void nextPlayer(int currentNr) {
        if (currentNr < mNrOfPlayers) {
            mCurrentPlayerNr = currentNr + 1;
        } else {
            mCurrentPlayerNr = 1;
        }
        setPlayerName(mCurrentPlayerNr);
    }

    /**
     * Initialize new player, if score of player was null.
     *
     * @param playerNr nr of player
     * @param playerName name of player
     * @return initialized Score
     */
    private Score initializePlayer(int playerNr, String playerName) {
        List<Float> distances = new ArrayList<>();
        Score score = new Score(playerName, 0, distances, mMapName);
        mScores.put(playerNr, score);
        return score;
    }

    /**
     * Shows result for round.
     *
     * @param resultPoint coordinates of result
     */
    private void showResult(LatLng resultPoint) {
        mShowAnswer = true;

        final String picPath = mapStore.getRootPath() + "/" + mapGoogle.getLocationPicturePath(mCurrentQuestionNr);
        Bundle bundle = new Bundle();
        bundle.putString(ResultDialog.IMAGE_PATH, picPath);

        // Show result dialog.
        final ResultDialog dialog = new ResultDialog();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), RESULT_DIALOG);

        ImageView questionPic = (ImageView) findViewById(R.id.image_preview);
        mPlayerTv.setText(getString(R.string.answer));
        questionPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show(getSupportFragmentManager(), RESULT_DIALOG);
            }
        });

        // Set result marker.
        mMap.addMarker(new MarkerOptions()
                .position(resultPoint)
                .title(getString(R.string.result))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(resultPoint));

        // Set markers of all players.
        Set<String> keys = mPoints.keySet();
        for (String key : keys) {
            LatLng point = mPoints.get(key);
            mMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title(key)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }
}
