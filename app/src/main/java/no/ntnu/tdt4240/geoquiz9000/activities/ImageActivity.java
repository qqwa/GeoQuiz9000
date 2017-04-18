package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;

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
import no.ntnu.tdt4240.geoquiz9000.dialogs.ResultDialog;
import no.ntnu.tdt4240.geoquiz9000.models.MapPicture;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;
import no.ntnu.tdt4240.geoquiz9000.models.Score;
import no.ntnu.tdt4240.geoquiz9000.ui.PinView;
import no.ntnu.tdt4240.geoquiz9000.utils.GeoUtils;

public class ImageActivity extends AbstractQuestionsActivity {

    private static final String TAG = ImageActivity.class.getSimpleName();

    private PinView mMapView;
    private MapPicture mMapPicture;
    private float mCurrentCalculatedDistance;
    private FloatingActionButton mActionButton;
    private TextView mPlayerTv;
    private Map<String, PointF> mPoints = new HashMap<>();
    private PointF mCurrentPoint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mActionButton = (FloatingActionButton) findViewById(R.id.action_button);
        mActionButton.setOnClickListener(onActionButtonClick);

        mPlayerTv = (TextView) findViewById(R.id.player_id);
        mCurrentPlayerNr = 1;
        setPlayerName(mCurrentPlayerNr);

        loadMapPackage();
        if (mMapStore != null && mMapPicture != null) {
            mCurrentQuestionNr = 0;
            initializeQuestion(0);
        }

        mMapView = (PinView) findViewById(R.id.map_image);
        String rootPath = mMapPicture.getRootPath();
        Bitmap map = BitmapFactory.decodeFile(rootPath + "/" + mMapPicture.getMap());
        setupMap(map);
    }

    /**
     * Sets up the image map.
     *
     * @param map Bitmap representation of map.
     */
    private void setupMap(Bitmap map) {
        mMapView.setImage(ImageSource.bitmap(map));

        final GestureDetector gestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public void onLongPress(MotionEvent e) {
                        if (mMapView.isReady()) {
                            PointF sCoord = mMapView.viewToSourceCoord(e.getX(), e.getY());
                            mMapView.setPin(sCoord);
                            mCurrentPoint = sCoord;

                            float answerX = (float) mMapPicture.getLocationX(mCurrentQuestionNr);
                            float answerY = (float) mMapPicture.getLocationY(mCurrentQuestionNr);
                            PointF answerPoint = new PointF(answerX, answerY);
                            float distance = GeoUtils.distanceBetweenTwoPoints(mMapPicture,
                                    answerPoint, sCoord);

                            Log.d(TAG, "Distance in meters: " + String.valueOf(distance));
                            mCurrentCalculatedDistance = distance;

                            mActionButton.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getApplicationContext(), "Long press: Image not ready",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (!mShowAnswer) {
                            mActionButton.setVisibility(View.GONE);
                            mMapView.clear();
                        }

                        mCurrentCalculatedDistance = 0;
                        mCurrentPoint = null;
                        return super.onSingleTapConfirmed(e);
                    }
                });

        mMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    /**
     * Load map package.
     */
    private void loadMapPackage() {
        Box maps = DatabaseLayer.getInstance(this).getBoxFor(MapStore.class);

        try {
            if (maps.find("name", mMapName).size() == 0) {
                //TODO: update me to new map management system
//                MapFactory.importMap(getAssets().open("wraeclast123.zip"), this).save(this);
            }
        } catch (Exception e) {
            Log.e(TAG, "Could not import map package.");
        }

        try {
            if (maps.find("name", mMapName).size() != 0) {
                mMapStore = (MapStore) maps.find("name", mMapName).get(0);
                mMapPicture = (MapPicture) MapFactory.getMap(mMapStore);
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not load map package.");
            Toast.makeText(this, getString(R.string.error_load_map_package), Toast.LENGTH_LONG)
                    .show();
        }
    }

    View.OnClickListener onNextQuestionClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mShowAnswer = false;

            int maxNrOfQuestions = mMapPicture.getLocationCount();
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
            int maxNrOfQuestions = mMapPicture.getLocationCount();

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
                    mMapView.clear();

                    float answerX = (float) mMapPicture.getLocationX(mCurrentQuestionNr);
                    float answerY = (float) mMapPicture.getLocationY(mCurrentQuestionNr);
                    PointF answerPoint = new PointF(answerX, answerY);
                    showResult(answerPoint);

                    mActionButton.setVisibility(View.VISIBLE);
                    mActionButton.setOnClickListener(onNextQuestionClick);
                } else {
                    mMapView.clear();

                    mActionButton.setVisibility(View.GONE);
                    initializeQuestion(mCurrentQuestionNr);
                }
            } else {
                nextPlayer(mCurrentPlayerNr);

                if (mCurrentPlayerNr != 1) {
                    mMapView.clear();
                    initializeQuestion(mCurrentQuestionNr);
                } else {
                    mMapView.clear();

                    float answerX = (float) mMapPicture.getLocationX(mCurrentQuestionNr);
                    float answerY = (float) mMapPicture.getLocationY(mCurrentQuestionNr);
                    PointF answerPoint = new PointF(answerX, answerY);
                    showResult(answerPoint);

                    mActionButton.setVisibility(View.VISIBLE);
                    mActionButton.setOnClickListener(onNextQuestionClick);
                }
            }
        }
    };

    /**
     * Initialize the question
     *
     * @param questionNr nr of question
     */
    private void initializeQuestion(final int questionNr) {
        final String picPath = mMapStore.getRootPath() + "/" + mMapPicture.getLocationPicture(questionNr);

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
     * Set up next player.
     *
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
     * Load next question.
     */
    private void nextQuestion() {
        mMapView.clear();

        mCurrentQuestionNr++;
        initializeQuestion(mCurrentQuestionNr);
    }

    /**
     * Set the player name in the box.
     *
     * @param nr player number
     */
    private void setPlayerName(int nr) {
        String playerNumber = String.valueOf(nr);
        mCurrentPlayer = getString(R.string.player) + " " + playerNumber;
        mPlayerTv.setText(mCurrentPlayer);
    }

    /**
     * Shows result for round.
     *
     * @param resultPoint coordinates of result
     */
    private void showResult(PointF resultPoint) {
        mShowAnswer = true;

        final String picPath = mMapStore.getRootPath() + "/" + mMapPicture.getLocationPicture(mCurrentQuestionNr);
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

        Map<PointF, PinView.Colors> pins = new HashMap<>();
        pins.put(resultPoint, PinView.Colors.GREEN);

        // Set markers of all players.
        Set<String> keys = mPoints.keySet();
        for (String key : keys) {
            PointF p = mPoints.get(key);
            pins.put(p, PinView.Colors.RED);
        }

        mMapView.setPins(pins);
    }

    public static Intent newIntent(Context c, String mapPackageName, int nrOfPlayers) {
        Intent intent = new Intent(c, ImageActivity.class);
        intent.putExtra(INTENT_PACKAGE_NAME, mapPackageName);
        intent.putExtra(INTENT_NR_OF_PLAYERS, nrOfPlayers);
        return intent;
    }
}
