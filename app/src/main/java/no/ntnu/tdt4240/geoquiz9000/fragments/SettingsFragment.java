package no.ntnu.tdt4240.geoquiz9000.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.activities.GeoActivity;
import no.ntnu.tdt4240.geoquiz9000.utils.GeoUtils;


public class SettingsFragment extends Fragment
{
    public interface Callbacks
    {
        void onMapPacksPressed();

        void onSettingsBackPressed();
    }

    private Callbacks m_callbacks;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        m_callbacks = (Callbacks)context;
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        m_callbacks = null;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final Typeface font = ((GeoActivity)getActivity()).getTextFont();
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        final Button backBtn = (Button)root.findViewById(R.id.back_btn);
        backBtn.setTypeface(font);
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_callbacks != null)
                    m_callbacks.onSettingsBackPressed();
            }
        });

        final Button mappacksBtn = (Button)root.findViewById(R.id.mappacks_btn);
        mappacksBtn.setTypeface(font);
        mappacksBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_callbacks != null) {
                    m_callbacks.onMapPacksPressed();
                }
            }
        });

        final Button unitsBtn = (Button)root.findViewById(R.id.units_btn);
        unitsBtn.setTypeface(font);
        unitsBtn.setText(getString(R.string.settings_units_label, GeoUtils.getCurrentUnits().toString()));
        unitsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (GeoUtils.getCurrentUnits()) {
                    case KM:
                        GeoUtils.setCurrentUnits(GeoUtils.Units.MILES);
                        unitsBtn.setText(getString(R.string.settings_units_label,
                                GeoUtils.Units.MILES.toString()));
                        return;
                    case MILES:
                        GeoUtils.setCurrentUnits(GeoUtils.Units.KM);
                        unitsBtn.setText(getString(R.string.settings_units_label,
                                GeoUtils.Units.KM.toString()));
                        return;
                }
            }
        });
        return root;
    }
}
