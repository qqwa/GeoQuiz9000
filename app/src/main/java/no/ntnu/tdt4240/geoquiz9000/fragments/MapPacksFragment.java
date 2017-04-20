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
import android.widget.ListView;

import java.util.List;

import io.objectbox.Box;
import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.activities.GeoActivity;
import no.ntnu.tdt4240.geoquiz9000.database.DatabaseLayer;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;
import no.ntnu.tdt4240.geoquiz9000.adapters.MapStoreArrayAdapter;

public class MapPacksFragment extends Fragment {
    public interface Callbacks {
        void onImportMapPressed();
        void onMapPacksBackPressed();
    }

    private Callbacks m_callbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        m_callbacks = (Callbacks)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        m_callbacks = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Typeface font = ((GeoActivity)getActivity()).getTextFont();
        View root = inflater.inflate(R.layout.fragment_map_packs, container, false);

        //TODO: Fill list
        Box mapBox = DatabaseLayer.getInstance(getActivity()).getBoxFor(MapStore.class);
        List<MapStore> stores = (List<MapStore>) mapBox.getAll();
        MapStoreArrayAdapter adapter = new MapStoreArrayAdapter(getContext(), stores);

        final ListView mapList = (ListView) root.findViewById(R.id.map_pack_list_view);
        mapList.setAdapter(adapter);

        final Button importMapBtn = (Button)root.findViewById(R.id.import_map_btn);
        importMapBtn.setTypeface(font);
        importMapBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_callbacks != null)
                    m_callbacks.onImportMapPressed();
            }
        });

        final Button backBtn = (Button)root.findViewById(R.id.back_btn);
        backBtn.setTypeface(font);
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_callbacks != null)
                    m_callbacks.onMapPacksBackPressed();
            }
        });

        return root;
    }
}
