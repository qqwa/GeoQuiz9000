package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.fragments.MultiplayerFragment;
import no.ntnu.tdt4240.geoquiz9000.fragments.SingleplayerFragment;

/**
 * Created by MikhailV on 20.03.2017.
 */

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

    // ---SinfleplayerFragment-CALLBACKS------------------------------------------------------------
    @Override
    public void onPlacePinPressed()
    {
        // TODO: 21.03.2017 pass arguments (target coordinates) to the MapsActivity through newIntent()
        startActivityForResult(MapsActivity.newIntent(QuestionActivity.this), REQUEST_MAPS);
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
        return singlePlayer ? SingleplayerFragment.newInstance(0) : new MultiplayerFragment();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_MAPS:
                float distance = MapsActivity.getDistance(data);
                int score = (int)(1f / distance); // TODO: 21.03.2017 calculate score
                final String resultMsg = getString(R.string.question_result, distance, score);

                // TODO: 04.04.2017 replace current fragment with the dialog showing answer
//                m_questionText.setText(resultMsg);
//                m_mapBtn.setText("Next question");
//                m_mapBtn.setOnClickListener(new NextQuestionClick());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
