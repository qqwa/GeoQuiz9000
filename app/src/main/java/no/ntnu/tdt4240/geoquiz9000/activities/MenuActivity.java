package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.io.InputStream;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.controllers.MapFactory;
import no.ntnu.tdt4240.geoquiz9000.dialogs.ImportErrorDialog;
import no.ntnu.tdt4240.geoquiz9000.fragments.AddPlayersFragment;
import no.ntnu.tdt4240.geoquiz9000.fragments.FrontpageFragment;
import no.ntnu.tdt4240.geoquiz9000.fragments.MapChooserFragment;
import no.ntnu.tdt4240.geoquiz9000.fragments.ScoreFragment;
import no.ntnu.tdt4240.geoquiz9000.fragments.SettingsFragment;

public class MenuActivity extends GeoActivity implements FrontpageFragment.Callbacks,
                                                         MapChooserFragment.Callbacks,
                                                         ScoreFragment.Callbacks,
                                                         SettingsFragment.Callbacks,
                                                         AddPlayersFragment.Callbacks
{
    private static final int REQUEST_FILE = 10;
    private static final int REQUEST_GAME = 11;
    private static final String SAVED_TITLE = "MenuActivity.SAVED_TITLE";
    private static final String TAG_ERROR_DIALOG = "MapChooserFragment.TAG_ERROR_DIALOG";

    private boolean m_gotoFrontpage = false;
    private boolean m_showErrorDialog = false;
    private String m_title;
    private int m_numberPlayers;

    // ---SettingsFragment-CALLBACKS----------------------------------------------------------------
    @Override
    public void onSettingsBackPressed()
    {
        m_title = getResources().getString(R.string.app_name);
        gotoPreviousState();
    }
    // ---ScoreFragment-CALLBACKS-------------------------------------------------------------------
    @Override
    public void onScoreBackPressed()
    {
        m_title = getResources().getString(R.string.app_name);
        gotoPreviousState();
    }
    // ---FrontpageFragment-CALLBACKS---------------------------------------------------------------
    @Override
    public void onSinglePlayerPressed()
    {
        m_numberPlayers = 1;
        replaceState(new MapChooserFragment());
    }
    @Override
    public void onMultiplayerPressed()
    {
        replaceState(new AddPlayersFragment());
    }
    @Override
    public void selectMapBtn(int nrOfPlayers) {
        m_numberPlayers = nrOfPlayers;
        replaceState(new MapChooserFragment());
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
        if (m_numberPlayers < 1) m_numberPlayers = 1;
        startActivity(MapsActivity.newIntent(this, "Test Map Pack", m_numberPlayers));
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
    protected void onResumeFragments()
    {
        // http://stackoverflow.com/a/15802094/4432988
        super.onResumeFragments();
        if (m_showErrorDialog) {
            m_showErrorDialog = false;
            new ImportErrorDialog().show(getSupportFragmentManager(), TAG_ERROR_DIALOG);
        }
        if (m_gotoFrontpage) {
            m_gotoFrontpage = false;
            replaceState(new FrontpageFragment());
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_TITLE, m_title);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_GAME:
                m_gotoFrontpage = true;
                break;
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
                            throw new Exception();
                        }
                    }
                    else {
                        throw new Exception();
                    }

                    // importing map
                    InputStream file = getContentResolver().openInputStream(path);
                    MapFactory.importMap(file, this).save(this);

                    // starting game, single player
                    startActivityForResult(QuestionActivity.newIntent(this, true), REQUEST_GAME);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    m_showErrorDialog = true;
                }
                finally {
                    if (cursor != null)
                        cursor.close();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
