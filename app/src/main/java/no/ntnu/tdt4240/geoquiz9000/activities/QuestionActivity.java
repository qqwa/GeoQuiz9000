package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.dialogs.AnswerDialog;
import no.ntnu.tdt4240.geoquiz9000.fragments.MultiplayerFragment;
import no.ntnu.tdt4240.geoquiz9000.fragments.SingleplayerFragment;


public class QuestionActivity extends GeoActivity implements SingleplayerFragment.Callbacks
{
    public static Intent newIntent(Context context, boolean singlePlayer)
    {
        Intent i = new Intent(context, QuestionActivity.class);
        i.putExtra(EXTRA_MODE, singlePlayer);
        return i;
    }
    private static final int REQUEST_MAPS = 0;
    private static final String EXTRA_MODE = "QuestionActivity.EXTRA_MODE";
    private static final String SAVED_QUESTION_ID = "QuestionActivity.SAVED_QUESTION_ID";
    private static final String TAG_ANSWER_DIALOG = "QuestionActivity.TAG_ANSWER_DIALOG";

    private boolean m_showAnswerDialog = false;
    private int m_score;
    private float m_distance;
    private int m_questionId = 0;

    // ---SingleplayerFragment-CALLBACKS------------------------------------------------------------
    @Override
    public void onPlacePinPressed()
    {
        //startActivityForResult(MapsActivity.newIntent(QuestionActivity.this), REQUEST_MAPS);
    }
    @Override
    public void onQuit(int currentPicId)
    {
        finish();
    }
    // ---LIFECYCLE-METHODS-------------------------------------------------------------------------
    @Override
    protected Fragment getInitialState()
    {
        boolean singlePlayer = getIntent().getBooleanExtra(EXTRA_MODE, true);
        return singlePlayer ? SingleplayerFragment.newInstance(m_questionId) : new MultiplayerFragment();
    }
    @Override
    protected CharSequence getTitleText()
    {
        return getResources().getString(R.string.question_title);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            m_questionId = savedInstanceState.getInt(SAVED_QUESTION_ID);
        setResult(RESULT_OK);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_QUESTION_ID, m_questionId);
    }
    @Override
    protected void onResumeFragments()
    {
        // http://stackoverflow.com/a/15802094/4432988
        super.onResumeFragments();
        if (m_showAnswerDialog) {
            m_showAnswerDialog = false;
            AnswerDialog.newInstance(0, m_distance, m_score)
                    .show(getSupportFragmentManager(), TAG_ANSWER_DIALOG);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_MAPS:
                m_distance = MapsActivity.getDistance(data);
                m_score = (int)(1f / m_distance); // TODO: 21.03.2017 calculate score
                m_showAnswerDialog = true;
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
