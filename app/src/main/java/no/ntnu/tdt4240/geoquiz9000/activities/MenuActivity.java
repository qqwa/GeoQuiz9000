package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.io.InputStream;

import io.objectbox.Box;
import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.controllers.MapFactory;
import no.ntnu.tdt4240.geoquiz9000.database.DatabaseLayer;
import no.ntnu.tdt4240.geoquiz9000.fragments.FrontpageFragment;
import no.ntnu.tdt4240.geoquiz9000.fragments.MapChooserFragment;
import no.ntnu.tdt4240.geoquiz9000.fragments.ScoreFragment;
import no.ntnu.tdt4240.geoquiz9000.fragments.SettingsFragment;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;

public class MenuActivity extends GeoActivity implements FrontpageFragment.Callbacks,
                                                         MapChooserFragment.Callbacks
{
    private static final int REQUEST_FILE = 10;
    private static final String SAVED_TITLE = "MenuActivity.SAVED_TITLE";

    private String m_title;
    // ---FrontpageFragment-CALLBACKS---------------------------------------------------------------
    @Override
    public void onSinglePlayerPressed()
    {
        replaceState(new MapChooserFragment());
    }
    @Override
    public void onMultiplayerPressed()
    {
        // TODO: 04.04.2017 start multiplayer game
    }
    @Override
    public void onSettingsPressed()
    {
        m_title = getResources().getString(R.string.settings_btn_label);
        replaceState(new SettingsFragment());
    }
    @Override
    public void onHighScorePressed()
    {
        m_title = getResources().getString(R.string.score_title_label);
        replaceState(new ScoreFragment());
    }
    @Override
    public void onQuitApplication()
    {
        finish();
    }
    // ---MapChooserFragment-CALLBACKS--------------------------------------------------------------
    @Override
    public void onDefaultMapPressed()
    {
        // importing map from assets
        try {
            Box maps = DatabaseLayer.getInstance(this).getBoxFor(MapStore.class);
            if (maps.find("name", "Test Map Pack").size() == 0) {
                MapFactory.importMap(getAssets().open("testMap.zip"), this).save(this);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        startActivity(MapsActivity.newIntent(this, "Test Map Pack", 1));
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
        gotoPreviousState();
    }
    // ---LIFECYCLE-METHODS-------------------------------------------------------------------------
    @Override
    protected Fragment getInitialState()
    {
        return new FrontpageFragment();
    }
    @Override
    protected CharSequence getTitleText()
    {
        return m_title;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
            m_title = savedInstanceState.getString(SAVED_TITLE);
        else
            m_title = getResources().getString(R.string.app_name);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
    {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(SAVED_TITLE, m_title);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_FILE:
                Cursor cursor = null;
                try {
                    Uri path = data.getData(); // might throw NullPointerException

                    // checking file type
                    cursor = getContentResolver().query(path, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        String fileName = cursor.getString(
                                cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        if (!fileName.endsWith(".zip") && !fileName.endsWith(".ZIP")) {
                            // TODO: 04.04.2017 show a reproaching dialog
                            return; // incorrect file type
                        }
                    }
                    else {
                        return; // no file
                    }

                    // importing map
                    InputStream file = getContentResolver().openInputStream(path);
                    MapFactory.importMap(file, this).save(this);

                    // starting game, single player
                    startActivity(QuestionActivity.newIntent(this, true));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (cursor != null)
                        cursor.close();
                }
                return;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
