package no.ntnu.tdt4240.geoquiz9000.fragments;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

import io.objectbox.Box;
import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.adapters.AbstractMapsAdapter;
import no.ntnu.tdt4240.geoquiz9000.database.DatabaseLayer;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;

public class MapChooserFragment extends AbstractListFragment<MapStore>
{
    public interface Callbacks
    {
        void onMapPressed(MapStore map);

        void onBackBtnPressed();
    }

    private Callbacks m_callbacks;

    @Override
    protected CharSequence getListLabel()
    {
        return getResources().getString(R.string.map_packs_info_label);
    }
    @Override
    protected ArrayAdapter<MapStore> getAdapter()
    {
        Box mapBox = DatabaseLayer.getInstance(getActivity()).getBoxFor(MapStore.class);
        List<MapStore> stores = (List<MapStore>)mapBox.getAll();
        return new AbstractMapsAdapter(getContext(), stores)
        {
            @Override
            protected void onMapPressed(View v, MapStore store)
            {
                if (m_callbacks != null)
                    m_callbacks.onMapPressed(store);
            }
        };
    }
    @Override
    protected void onBackPressed()
    {
        if (m_callbacks != null)
            m_callbacks.onBackBtnPressed();
    }
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
}
