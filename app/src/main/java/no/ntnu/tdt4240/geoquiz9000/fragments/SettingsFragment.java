package no.ntnu.tdt4240.geoquiz9000.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.activities.GeoActivity;

/**
 * Created by MikhailV on 04.04.2017.
 */

public class SettingsFragment extends Fragment
{
    // TODO: 04.04.2017 callbacks...

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final Typeface textFont = ((GeoActivity)getActivity()).getTextFont();
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        // TODO: 04.04.2017 set textt font and listeners ...

        return root;
    }
}
