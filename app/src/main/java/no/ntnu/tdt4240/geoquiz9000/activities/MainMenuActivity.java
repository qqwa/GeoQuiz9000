package no.ntnu.tdt4240.geoquiz9000.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import io.objectbox.Box;
import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.controllers.MapFactory;
import no.ntnu.tdt4240.geoquiz9000.database.DatabaseLayer;
import no.ntnu.tdt4240.geoquiz9000.models.MapGoogle;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;

public class MainMenuActivity extends GeoActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        final TextView title = (TextView)findViewById(R.id.main_title);
        title.setTypeface(getTitleFont());

        final Button singleplayerBtn = (Button)findViewById(R.id.singleplayer_btn);
        singleplayerBtn.setTypeface(getTextFont());
        singleplayerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO: 20.03.2017 start singleplayer game, f.ex.:
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
                // TODO: 20.03.2017 start multiplayer game
            }
        });
        final Button settingsBtn = (Button)findViewById(R.id.settings_btn);
        settingsBtn.setTypeface(getTextFont());
        settingsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(SettingsActivity.newIntent(MainMenuActivity.this));
            }
        });
        final Button highscoreBtn = (Button)findViewById(R.id.highscore_btn);
        highscoreBtn.setTypeface(getTextFont());
        highscoreBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(ScoreActivity.newIntent(MainMenuActivity.this));
            }
        });


        //TODO: remove this once the proper import menu is in place
        try {
            //import map
            Box maps = DatabaseLayer.getInstance(this).getBoxFor(MapStore.class);
            if(maps.find("name", "Test Map Pack").size() == 0){
                MapStore map = MapFactory.importMap(getAssets().open("testMap.zip"), this);
                map.save(this);
                Log.i("MainMenu", "Imported and Saved Map " + map.getName() + " to: " + map.getRootPath());
            }
            //load imported map
            if(maps.find("name", "Test Map Pack").size() != 0){
                MapStore testMap = (MapStore)maps.find("name", "Test Map Pack").get(0);
                MapGoogle mapGoogle = (MapGoogle)MapFactory.getMap(testMap);
                ImageView iv = new ImageView(this);
                iv.setImageBitmap(mapGoogle.getLocationPicture(0));
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.singleplayer_btn).getParent();
                linearLayout.addView(iv);
                Log.i("MainMenu", "Loaded Map: " + testMap.getRootPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
