package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import io.objectbox.Box;
import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.controllers.MapFactory;
import no.ntnu.tdt4240.geoquiz9000.database.DatabaseLayer;
import no.ntnu.tdt4240.geoquiz9000.fragments.FrontpageFragment;
import no.ntnu.tdt4240.geoquiz9000.fragments.MapChooserFragment;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;

public class MenuActivity extends GeoActivity implements FrontpageFragment.Callbacks,
                                                         MapChooserFragment.Callbacks
{
    private static final int REQUEST_FILE = 10;
    // ---FrontpageFragment-CALLBACKS---------------------------------------------------------------
    @Override
    public void onSinglePlayerPressed()
    {
        // TODO: 20.03.2017 start singleplayer game, f.ex.:
    }
    @Override
    public void onMultiplayerPressed()
    {
        // TODO: 20.03.2017 start multiplayer game
    }
    @Override
    public void onSettingsPressed()
    {
        // TODO: 04.04.2017 replace fragment
    }
    @Override
    public void onHighScorePressed()
    {
        startActivity(ScoreActivity.newIntent(MenuActivity.this));
    }
    // ---MapChooserFragment-CALLBACKS--------------------------------------------------------------
    @Override
    public void onDefaultMapPressed()
    {
        // importing map from assets
        try {
            Box maps = DatabaseLayer.getInstance(this).getBoxFor(MapStore.class);
            if (maps.find("name", "Test Map Pack").size() == 0) {
                MapStore map = MapFactory.importMap(getAssets().open("testMap.zip"), this);
                map.save(this);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBrowseMapPressed()
    {
        // launching file explorer
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_FILE);
    }
    @Override
    public void onBackBtnPressed()
    {
        onBackPressed();
    }
    // ---LIFECYCLE-METHODS-------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        replaceFragment(new FrontpageFragment()); // set initial fragment

//        //TODO: remove this once the proper import menu is in place
//        try {
//            //import map
//            Box maps = DatabaseLayer.getInstance(this).getBoxFor(MapStore.class);
//            if(maps.find("name", "Test Map Pack").size() == 0){
//                MapStore map = MapFactory.importMap(getAssets().open("testMap.zip"), this);
//                map.save(this);
//                Log.i("MainMenu", "Imported and Saved Map " + map.getName() + " to: " + map.getRootPath());
//            }
//            //load imported map
//            if(maps.find("name", "Test Map Pack").size() != 0){
//                MapStore testMap = (MapStore)maps.find("name", "Test Map Pack").get(0);
//                MapGoogle mapGoogle = (MapGoogle)MapFactory.getMap(testMap);
//                ImageView iv = new ImageView(this);
//                iv.setImageBitmap(mapGoogle.getLocationPicture(0));
//                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.singleplayer_btn).getParent();
//                linearLayout.addView(iv);
//                Log.i("MainMenu", "Loaded Map: " + testMap.getRootPath());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == REQUEST_FILE) {
            onBackPressed(); // switch back to menu fragment

            Uri path = data.getData();

            // TODO: 04.04.2017 load imported map

            // TODO: 04.04.2017 start QuestionActivity, f.ex:
            startActivity(QuestionActivity.newIntent(this));

        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
