package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import no.ntnu.tdt4240.geoquiz9000.R;


// TODO Mikhail has to refactor it.
public class QuestionActivity extends GeoActivity
{
    public static Intent newIntent(Context context, boolean singlePlayer)
    {
        Intent i = new Intent(context, QuestionActivity.class);
        i.putExtra(EXTRA_MODE, singlePlayer);
        return i;
    }
    private static final String EXTRA_MODE = "QuestionActivity.EXTRA_MODE";


    // ---LIFECYCLE-METHODS-------------------------------------------------------------------------
    @Override
    protected Fragment getInitialState()
    {
        return null;
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
        setResult(RESULT_OK);
    }
}
