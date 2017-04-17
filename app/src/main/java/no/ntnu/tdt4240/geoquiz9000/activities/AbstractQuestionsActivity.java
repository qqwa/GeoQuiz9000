package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.dialogs.PictureDialogFragment;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;
import no.ntnu.tdt4240.geoquiz9000.models.Score;

abstract class AbstractQuestionsActivity extends FragmentActivity {

    public static final String PIC_DIALOG = "picture dialog";
    public static final String RESULT_DIALOG = "result dialog";
    public static final String INTENT_PACKAGE_NAME = "intent package name";
    public static final String INTENT_NR_OF_PLAYERS = "intent nr of players";

    public Map<Integer, Score> mScores = new HashMap<>();
    public String mMapName;
    public MapStore mMapStore;
    public String mCurrentPlayer;
    public int mNrOfPlayers;
    public int mCurrentPlayerNr;
    public boolean mShowAnswer;
    public int mCurrentQuestionNr;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMapName = getIntent().getStringExtra(INTENT_PACKAGE_NAME);
        mNrOfPlayers = getIntent().getIntExtra(INTENT_NR_OF_PLAYERS, 1);
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
                        AbstractQuestionsActivity.super.onBackPressed();
                        finish();
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * Show question and picture again
     *
     * @param questionNr nr of question
     * @param picPath    path to picture
     */
    public void showDialog(int questionNr, String picPath) {
        Bundle bundle = new Bundle();
        bundle.putInt(PictureDialogFragment.NR_OF_QUESTION, questionNr);
        bundle.putString(PictureDialogFragment.IMAGE_PATH, picPath);
        bundle.putString(PictureDialogFragment.PLAYER_NAME, mCurrentPlayer);

        PictureDialogFragment dialog = new PictureDialogFragment();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), PIC_DIALOG);
    }

    /**
     * Initialize new player, if score of player was null.
     *
     * @param playerNr   nr of player
     * @param playerName name of player
     * @return initialized Score
     */
    public Score initializePlayer(int playerNr, String playerName) {
        List<Float> distances = new ArrayList<>();
        Score score = new Score(playerName, 0, distances, mMapName);
        mScores.put(playerNr, score);
        return score;
    }
}
