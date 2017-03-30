package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;

import no.ntnu.tdt4240.geoquiz9000.R;

/**
 * Created by MikhailV on 21.03.2017.
 */

public class SettingsActivity extends GeoActivity
{
    public static Intent newIntent(Context context)
    {
        return new Intent(context, SettingsActivity.class);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat
    {
        @Override
        public void onCreatePreferences(Bundle bundle, String s)
        {
            setPreferencesFromResource(R.xml.fragment_settings, s);
        }
    }
}
