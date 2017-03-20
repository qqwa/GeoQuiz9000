package no.ntnu.tdt4240.geoquiz9000.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import no.ntnu.tdt4240.geoquiz9000.R;

public class MainMenuActivity extends GeoActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        TextView title = (TextView)findViewById(R.id.main_title);
        title.setTypeface(getTitleFont());

        final Button singleplayerBtn = (Button)findViewById(R.id.singleplayer_btn);
        singleplayerBtn.setTypeface(getTextFont());
        singleplayerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO: 20.03.2017
                startActivity(QuestionActivity.newIntent(MainMenuActivity.this));
            }
        });
        final Button multiplayerBtn = (Button)findViewById(R.id.multiplayer_btn);
        multiplayerBtn.setTypeface(getTextFont());
        multiplayerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO: 20.03.2017
            }
        });
        final Button settingsBtn = (Button)findViewById(R.id.settings_btn);
        settingsBtn.setTypeface(getTextFont());
        settingsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO: 20.03.2017
            }
        });
        final Button highscoreBtn = (Button)findViewById(R.id.highscore_btn);
        highscoreBtn.setTypeface(getTextFont());
        highscoreBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO: 20.03.2017
            }
        });
    }
}
