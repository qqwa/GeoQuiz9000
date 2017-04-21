package no.ntnu.tdt4240.geoquiz9000.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;

import no.ntnu.tdt4240.geoquiz9000.R;

public class UiUtils {

    /**
     * @param c Context
     * @return standard text font
     */
    public static Typeface getTextFont(Context c) {
        String textFontName = c.getResources().getString(R.string.text_font_name);
        return Typeface.createFromAsset(c.getAssets(), textFontName);
    }

    /**
     * @param c Context
     * @return standard title font
     */
    public static Typeface getTitleFont(Context c) {
        String titleFontName = c.getResources().getString(R.string.title_font_name);
        return Typeface.createFromAsset(c.getAssets(), titleFontName);
    }
}
