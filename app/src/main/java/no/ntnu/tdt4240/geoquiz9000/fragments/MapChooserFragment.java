package no.ntnu.tdt4240.geoquiz9000.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.activities.GeoActivity;

/**
 * Created by MikhailV on 04.04.2017.
 */

public class MapChooserFragment extends Fragment
{
    public interface Callbacks
    {
        void onDefaultMapPressed();

        void onBrowseMapPressed();

        void onBackBtnPressed();
    }

    private Callbacks m_callbacks;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        m_callbacks = (Callbacks)getActivity();
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        m_callbacks = null;
    }
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final Typeface textFont = ((GeoActivity)getActivity()).getTextFont();
        View root = inflater.inflate(R.layout.fragment_map_chooser, container, false);

        final Button defaultBtn = (Button)root.findViewById(R.id.default_map_btn);
        defaultBtn.setTypeface(textFont);
        defaultBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_callbacks != null) {
                    m_callbacks.onDefaultMapPressed();
                }

            }
        });
        final Button browseBtn = (Button)root.findViewById(R.id.browse_map_btn);
        browseBtn.setTypeface(textFont);
        browseBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_callbacks != null) {
                    m_callbacks.onBrowseMapPressed();
                }
            }
        });
        final Button backBtn = (Button)root.findViewById(R.id.back_btn);
        backBtn.setTypeface(textFont);
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_callbacks != null) {
                    m_callbacks.onBackBtnPressed();
                }
            }
        });
        return root;
    }
}
