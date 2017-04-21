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
    public interface OnPopupListener
    {
        boolean onPopupItemClick(MapStore store, MenuItem item);
    }

    private OnPopupListener m_callback;

    public MapStoreArrayAdapter(Context context, List<MapStore> stores, OnPopupListener callback)
    {
        super(context, stores);
        m_callback = callback;
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
                return m_callback.onPopupItemClick(store, item);
            }
        });
        popup.show();
    }
}
