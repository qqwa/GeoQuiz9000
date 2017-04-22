package no.ntnu.tdt4240.geoquiz9000.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import io.objectbox.Box;
import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.controllers.AsyncImportMap;
import no.ntnu.tdt4240.geoquiz9000.database.DatabaseLayer;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;

public abstract class GeoActivity extends AppCompatActivity
{
    private static final String SAVED_FRAGMENT = "GeoActivity.SAVED_FRAGMENT";
    private static final String SAVED_DEFAULTS_IMPORTED = "GeoActivity.SAVED_DEFAULTS_IMPORTED";

    private boolean m_defaultMapsImported = false;
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

        // importing default maps from assets
        if (savedInstanceState != null) {
            m_defaultMapsImported = savedInstanceState.getBoolean(SAVED_DEFAULTS_IMPORTED);
        }
        if (!m_defaultMapsImported) {
            try {
                Box maps = DatabaseLayer.getInstance(this).getBoxFor(MapStore.class);
                if (maps.find("name", "Test Map Pack").size() == 0) {
                    m_defaultMapsImported = true;
                    new AsyncImportMap(getAssets().open("testMap.zip"), this)
                    {
                        @Override
                        protected void onCancelled() { m_defaultMapsImported = false; }
                        @Override
                        protected void onPostExecute(MapStore mapStore)
                        {
                            try {
                                new AsyncImportMap(getAssets().open("wraeclast3.zip"), GeoActivity.this)
                                {
                                    @Override
                                    protected void onCancelled() { m_defaultMapsImported = false; }
                                }.execute();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        Fragment currentState = getSupportFragmentManager().findFragmentById(R.id.container);
        if (currentState != null) {
            getSupportFragmentManager().putFragment(outState, SAVED_FRAGMENT, currentState);
        }
        outState.putBoolean(SAVED_DEFAULTS_IMPORTED, m_defaultMapsImported);
    }
    protected void gotoPreviousState()
    {
        m_title.setText(getTitleText());
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1)
            fm.popBackStack();
    }
    protected Fragment getCurrentState()
    {
        return getSupportFragmentManager().findFragmentById(R.id.container);
    }
    @Override
    public void onBackPressed()
    {
        // disable back button
    }
}
