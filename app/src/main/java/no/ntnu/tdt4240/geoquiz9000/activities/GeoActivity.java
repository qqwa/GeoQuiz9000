package no.ntnu.tdt4240.geoquiz9000.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import no.ntnu.tdt4240.geoquiz9000.R;

public abstract class GeoActivity extends AppCompatActivity
{
    private static final String SAVED_FRAGMENT = "GeoActivity.SAVED_FRAGMENT";

    private Typeface m_textFont;
    private Typeface m_titleFont;
    private TextView m_title;

    public Typeface getTextFont()
    {
        return m_textFont;
    }
    public Typeface getTitleFont()
    {
        return m_titleFont;
    }
    protected void replaceState(Fragment nextState)
    {
        m_title.setText(getTitleText());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, nextState)
                .addToBackStack(null)
                .commit();
    }
    protected abstract Fragment getInitialState();

    protected abstract CharSequence getTitleText();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        String textFontName = getResources().getString(R.string.text_font_name);
        m_textFont = Typeface.createFromAsset(getAssets(), textFontName);
        String titleFontName = getResources().getString(R.string.title_font_name);
        m_titleFont = Typeface.createFromAsset(getAssets(), titleFontName);

        setContentView(R.layout.activity_geo);

        m_title = (TextView)findViewById(R.id.main_title);
        m_title.setTypeface(getTitleFont());
        m_title.setText(getTitleText());

        Fragment state = savedInstanceState == null ? getInitialState()
                : getSupportFragmentManager().getFragment(savedInstanceState, SAVED_FRAGMENT);
        replaceState(state);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        Fragment currentState = getSupportFragmentManager().findFragmentById(R.id.container);
        if (currentState != null) {
            getSupportFragmentManager().putFragment(outState, SAVED_FRAGMENT, currentState);
        }
    }
    protected void gotoPreviousState()
    {
        m_title.setText(getTitleText());
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1)
            fm.popBackStack();
    }
    @Override
    public void onBackPressed()
    {
        // disable back button
    }
}
