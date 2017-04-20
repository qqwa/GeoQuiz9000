package no.ntnu.tdt4240.geoquiz9000.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;

public class MapStoreArrayAdapter extends AbstractMapsAdapter
{
    public MapStoreArrayAdapter(Context context, List<MapStore> stores)
    {
        super(context, stores);
    }
    @Override
    protected void onMapPressed(View v, final MapStore store)
    {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenuInflater().inflate(R.menu.menu_map_pack_option, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getItemId()) {
                    case R.id.menu_add_question:
                        //TODO: add question
                        Log.i("MENU", "ADD QUESTION:" + store.getName());
                        return true;
                    case R.id.menu_export_map:
                        //TODO: export map
                        Log.i("MENU", "EXPORT MAP:" + store.getName());
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }
}
