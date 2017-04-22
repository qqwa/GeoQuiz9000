package no.ntnu.tdt4240.geoquiz9000.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.controllers.AsyncAddQuestion;
import no.ntnu.tdt4240.geoquiz9000.controllers.AsyncExportMap;
import no.ntnu.tdt4240.geoquiz9000.controllers.AsyncImportMap;
import no.ntnu.tdt4240.geoquiz9000.dialogs.EnterUrlDialog;
import no.ntnu.tdt4240.geoquiz9000.dialogs.ImportErrorDialog;
import no.ntnu.tdt4240.geoquiz9000.dialogs.LocationDialog;
import no.ntnu.tdt4240.geoquiz9000.fragments.AddPlayersFragment;
import no.ntnu.tdt4240.geoquiz9000.dialogs.TaskDialog;
import no.ntnu.tdt4240.geoquiz9000.fragments.FrontpageFragment;
import no.ntnu.tdt4240.geoquiz9000.fragments.MapChooserFragment;
import no.ntnu.tdt4240.geoquiz9000.fragments.MapPacksFragment;
import no.ntnu.tdt4240.geoquiz9000.fragments.ScoreFragment;
import no.ntnu.tdt4240.geoquiz9000.fragments.SettingsFragment;
import no.ntnu.tdt4240.geoquiz9000.models.IMap;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;

public class MenuActivity extends GeoActivity implements FrontpageFragment.Callbacks,
                                                         MapChooserFragment.Callbacks,
                                                         ScoreFragment.Callbacks,
                                                         SettingsFragment.Callbacks,
                                                         AddPlayersFragment.Callbacks,
                                                         MapPacksFragment.Callbacks,
                                                         TaskDialog.Callbacks,
                                                         EnterUrlDialog.Callbacks,
                                                         LocationDialog.Callbacks
{
    public static Intent startMapChooserIntent(Context c, int nrOfPlayers)
    {
        Intent intent = new Intent(c, MenuActivity.class);
        intent.putExtra(INTENT_NR_OF_PLAYERS, nrOfPlayers);
        return intent;
    }
    private static final int REQUEST_FILE = 10;
    private static final int REQUEST_GAME = 11;
    private static final int REQUEST_PICTURE = 12;
    private static final int PERMISSION_REQUEST_WRITE = 20;
    private static final String SAVED_TITLE = "MenuActivity.SAVED_TITLE";
    private static final String TAG_ERROR_DIALOG = "MapChooserFragment.TAG_ERROR_DIALOG";
    private static final String TAG_URL_DIALOG = "MenuActivity.TAG_URL_DIALOG";
    private static final String TAG_LOCATION_DIALOG = "MenuActivity.TAG_LOCATION_DIALOG";
    private static final String INTENT_NR_OF_PLAYERS = "intent nr of players";
    private static final String TAG_TASK_DIALOG = "MapPacksFragment.TAG_TASK_DIALOG";

    private boolean m_gotoFrontpage = false;
    private boolean m_showErrorDialog = false;
    private boolean m_showTaskDialog = false;
    private AsyncTask m_task = null;
    private InputStream m_file = null;
    private String m_title;
    private int m_numberPlayers;

    private MapStore m_selectedMapStore = null;
    private Bitmap m_selectedPicture = null;
    private boolean m_showLocationGoogleDialog = false;
    private boolean m_showLocationPictureDialog = false;

    // ---LocationDialog-CALLBACKS------------------------------------------------------------------
    @Override
    public void onDialogDismissed()
    {
        m_selectedPicture = null;
        m_selectedMapStore = null;
    }
    @Override
    public void onLocationSubmitted(IMap.Location location)
    {
        if (m_selectedPicture != null && m_selectedMapStore != null && location != null) {
            new AsyncAddQuestion(m_selectedMapStore, m_selectedPicture, location)
            {
                @Override
                protected void onPostExecute(MapStore mapStore)
                {
                    Toast.makeText(getApplicationContext(), "Picture added successfully", Toast.LENGTH_SHORT)
                            .show();
                }
            }.execute();
        }
        m_selectedPicture = null;
        m_selectedMapStore = null;
    }
    // ---EnterUrlDialog-CALLBACKS------------------------------------------------------------------
    @Override
    public void onUrlSubmitted(String url)
    {
        final TaskDialog taskDialog = TaskDialog.newInstance();
        //Prevent user from closing dialog until task is finished.
        taskDialog.setCancelable(false);
        taskDialog.show(getSupportFragmentManager(), TAG_TASK_DIALOG);

        Log.i("MENU", "CREATING TASK FOR URL " + url);
        new AsyncImportMap(url, this)
        {
            @Override
            protected void onPostExecute(MapStore mapStore)
            {
                if (mapStore != null) {
                    taskDialog.setTaskLog("Successfully imported Map!");
                }
                else {
                    taskDialog.setTaskLog(getErrorMessage());
                }
                taskDialog.setCanDismiss(true);
            }
            @Override
            protected void onCancelled()
            {
                taskDialog.setTaskLog("Canceled.");
                taskDialog.setCanDismiss(true);
            }
            @Override
            protected void onProgressUpdate(String... values)
            {
                taskDialog.setTaskLog(values[0]);
            }
        }.execute();
        Log.i("MENU", "EXECUTING TASK");
    }
    // ---TaskDialog-CALLBACKS----------------------------------------------------------------------
    @Override
    public void onCancelPressed()
    {
        if (m_task != null) {
            m_task.cancel(false);
        }
        updateMapPacksList();
    }
    @Override
    public void onOkPressed()
    {
        updateMapPacksList();
    }
    // ---MapPacksFragment-CALLBACKS----------------------------------------------------------------
    @Override
    public void onAddPicturePressed(MapStore store)
    {
        m_selectedMapStore = store;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_pic_prompt)),
                REQUEST_PICTURE);
    }
    @Override
    public void onExportMapPressed(MapStore store)
    {
        if (m_selectedMapStore != null) {
            Toast.makeText(getApplicationContext(), "Cannot export map now!", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        m_selectedMapStore = store;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    PERMISSION_REQUEST_WRITE);
        }
        else {
            writeMapToFile();
            m_selectedMapStore = null;
        }
    }
    @Override
    public void onImportMapPressed()
    {
        Button button = (Button)findViewById(R.id.import_map_btn);
        PopupMenu popup = new PopupMenu(MenuActivity.this, button);
        popup.getMenuInflater().inflate(R.menu.menu_import_option, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getItemId()) {
                    case R.id.menu_import_storage:
                        Log.i("MENU", "IMPORT FROM DEVICE");
                        // launching file explorer
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        intent.setType("*/*");
                        startActivityForResult(intent, REQUEST_FILE);
                        return true;
                    case R.id.menu_import_network:
                        Log.i("MENU", "IMPORT FROM NETWORK");
                        EnterUrlDialog dialog = new EnterUrlDialog();
                        dialog.show(getSupportFragmentManager(), TAG_URL_DIALOG);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();
    }
    @Override
    public void onMapPacksBackPressed()
    {
        m_title = getResources().getString(R.string.app_name);
        gotoPreviousState();
    }
    // ---SettingsFragment-CALLBACKS----------------------------------------------------------------
    @Override
    public void onSettingsBackPressed()
    {
        m_title = getResources().getString(R.string.app_name);
        gotoPreviousState();
    }
    @Override
    public void onMapPacksPressed()
    {
        m_title = getResources().getString(R.string.mappacks_btn_label);
        replaceState(new MapPacksFragment());
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
    public void onSettingsPressed()
    {
        m_title = getResources().getString(R.string.settings_title_label);
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
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.quit_game))
                .setMessage(getString(R.string.sure_to_quit_game))
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { dialogInterface.dismiss(); }
                })
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { finish(); }
                })
                .create();
        dialog.show();
    }
    // ---MapChooserFragment-CALLBACKS--------------------------------------------------------------
    @Override
    public void onMapPressed(MapStore map)
    {
        if (m_numberPlayers < 1) m_numberPlayers = 1;

        if (map.getType() == IMap.MapType.GOOGLE) {
            startActivity(MapsActivity.newIntent(this, map.getName(), m_numberPlayers));
        }
        else if (map.getType() == IMap.MapType.PICTURE) {
            startActivity(ImageActivity.newIntent(this, map.getName(), m_numberPlayers));
        }
    }
    @Override
    public void onBackBtnPressed() // also for AddPlayersFragment
    {
        gotoPreviousState();
    }
    // ---AddPlayersFragment-CALLBACKS--------------------------------------------------------------
    @Override
    public void selectMapBtn(int nrOfPlayers)
    {
        m_numberPlayers = nrOfPlayers;
        replaceState(new MapChooserFragment());
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

        int nrOfPlayers = getIntent().getIntExtra(INTENT_NR_OF_PLAYERS, 0);
        if (nrOfPlayers > 0) {
            m_numberPlayers = nrOfPlayers;
            replaceState(new MapChooserFragment());
        }
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
        if (m_showLocationGoogleDialog) {
            m_showLocationGoogleDialog = false;
            LocationDialog.newLocationGoogleDialog().show(getSupportFragmentManager(), TAG_LOCATION_DIALOG);
        }
        if (m_showLocationPictureDialog) {
            m_showLocationPictureDialog = false;
            LocationDialog.newLocationPictureDialog().show(getSupportFragmentManager(), TAG_LOCATION_DIALOG);
        }
        if (m_showTaskDialog) {
            m_showTaskDialog = false;
            final TaskDialog taskDialog = TaskDialog.newInstance();
            //Prevent user from closing dialog until task is finished.
            taskDialog.setCancelable(false);
            taskDialog.show(getSupportFragmentManager(), TAG_TASK_DIALOG);

            if (m_file != null) {
                Log.i("MENU", "CREATING TASK FOR FILE");
                new AsyncImportMap(m_file, this)
                {
                    @Override
                    protected void onPostExecute(MapStore mapStore)
                    {
                        if (mapStore != null) {
                            taskDialog.setTaskLog("Successfully imported Map!");
                        }
                        else {
                            taskDialog.setTaskLog(getErrorMessage());
                        }
                        taskDialog.setCanDismiss(true);
                    }
                    @Override
                    protected void onCancelled()
                    {
                        taskDialog.setTaskLog("Canceled.");
                        taskDialog.setCanDismiss(true);
                    }
                    @Override
                    protected void onProgressUpdate(String... values)
                    {
                        taskDialog.setTaskLog(values[0]);
                    }
                }.execute();
                m_file = null;
            }
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
            case REQUEST_PICTURE:
                try {
                    if (m_selectedMapStore == null)
                        throw new IllegalStateException();
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    m_selectedPicture = BitmapFactory.decodeStream(inputStream);

                    if (m_selectedMapStore.getType() == IMap.MapType.GOOGLE)
                        m_showLocationGoogleDialog = true;
                    else if (m_selectedMapStore.getType() == IMap.MapType.PICTURE)
                        m_showLocationPictureDialog = true;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    m_showErrorDialog = true;
                    m_selectedMapStore = null;
                    m_selectedPicture = null;
                }
                break;
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
                    m_file = getContentResolver().openInputStream(path);
                    m_showTaskDialog = true;
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PERMISSION_REQUEST_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writeMapToFile();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error: permission denied", Toast.LENGTH_SHORT)
                            .show();
                }
                m_selectedMapStore = null;
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void writeMapToFile() // doesn't clear m_selectedMapStore
    {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        dir.mkdirs();
        String fname = m_selectedMapStore.getName() + ".zip";
        File file = new File(dir, fname);
        new AsyncExportMap(m_selectedMapStore, file)
        {
            @Override
            protected void onPostExecute(Void aVoid)
            {
                Toast.makeText(getApplicationContext(), "Map exported successfully to Downloads", Toast.LENGTH_SHORT)
                        .show();
            }
            @Override
            protected void onCancelled()
            {
                Toast.makeText(getApplicationContext(), "Error: export cancelled", Toast.LENGTH_SHORT)
                        .show();
            }
        }.execute();
    }
    private void updateMapPacksList()
    {
        Fragment state = getCurrentState();
        if (state instanceof MapPacksFragment) {
            ((MapPacksFragment)state).updateListView();
            Log.d("Maps", "List view updated from callback");
        }
    }
}
