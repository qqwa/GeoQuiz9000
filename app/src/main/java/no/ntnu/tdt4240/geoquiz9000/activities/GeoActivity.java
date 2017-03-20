package no.ntnu.tdt4240.geoquiz9000.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import no.ntnu.tdt4240.geoquiz9000.R;

/**
 * Created by MikhailV on 20.03.2017.
 */

class GeoActivity extends AppCompatActivity
{
    private Typeface m_textFont;
    private Typeface m_titleFont;

    protected Typeface getTextFont()
    {
        return m_textFont;
    }
    protected Typeface getTitleFont()
    {
        return m_titleFont;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        final String textFontName = getResources().getString(R.string.text_font_name);
        m_textFont = Typeface.createFromAsset(getAssets(), textFontName);

        final String titleFontName=getResources().getString(R.string.title_font_name);
        m_titleFont= Typeface.createFromAsset(getAssets(), titleFontName);
    }
}
