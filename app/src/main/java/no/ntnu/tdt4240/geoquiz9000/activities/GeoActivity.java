package no.ntnu.tdt4240.geoquiz9000.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import no.ntnu.tdt4240.geoquiz9000.R;

/**
 * Created by MikhailV on 20.03.2017.
 */

public class GeoActivity extends AppCompatActivity
{
    private Typeface m_textFont;
    private Typeface m_titleFont;

    public Typeface getTextFont()
    {
        return m_textFont;
    }
    public Typeface getTitleFont()
    {
        return m_titleFont;
    }
    protected void replaceFragment(Fragment newFragment)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, newFragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        String textFontName = getResources().getString(R.string.text_font_name);
        m_textFont = Typeface.createFromAsset(getAssets(), textFontName);
        String titleFontName = getResources().getString(R.string.title_font_name);
        m_titleFont = Typeface.createFromAsset(getAssets(), titleFontName);

        setContentView(R.layout.activity_geo);

        final TextView title = (TextView)findViewById(R.id.main_title);
        title.setTypeface(getTitleFont());
    }
    public void onBackPressed()
    {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
}
