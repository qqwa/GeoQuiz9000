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

public class FrontpageFragment extends Fragment
{
    public interface Callbacks
    {
        void onSinglePlayerPressed();

        void onMultiplayerPressed();

        void onSettingsPressed();

        void onHighScorePressed();

        void onQuitApplication();
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final Typeface textFont = ((GeoActivity)getActivity()).getTextFont();
        View root = inflater.inflate(R.layout.fragment_frontpage, container, false);

        final Button singleplayerBtn = (Button)root.findViewById(R.id.singleplayer_btn);
        singleplayerBtn.setTypeface(textFont);
        singleplayerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_callbacks != null) {
                    m_callbacks.onSinglePlayerPressed();
                }
            }
        });
        final Button multiplayerBtn = (Button)root.findViewById(R.id.multiplayer_btn);
        multiplayerBtn.setTypeface(textFont);
        multiplayerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_callbacks != null) {
                    m_callbacks.onMultiplayerPressed();
                }
            }
        });

        final Button settingsBtn = (Button)root.findViewById(R.id.settings_btn);
        settingsBtn.setTypeface(textFont);
        settingsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_callbacks != null) {
                    m_callbacks.onSettingsPressed();
                }
            }
        });
        final Button highscoreBtn = (Button)root.findViewById(R.id.highscore_btn);
        highscoreBtn.setTypeface(textFont);
        highscoreBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_callbacks != null) {
                    m_callbacks.onHighScorePressed();
                }
            }
        });
        final Button quitBtn = (Button)root.findViewById(R.id.quit_btn);
        quitBtn.setTypeface(textFont);
        quitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_callbacks != null) {
                    m_callbacks.onQuitApplication();
                }
            }
        });
        return root;
    }
}
