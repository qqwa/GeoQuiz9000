package no.ntnu.tdt4240.geoquiz9000.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.List;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;
import no.ntnu.tdt4240.geoquiz9000.ui.UiUtils;


public abstract class AbstractMapsAdapter extends ArrayAdapter<MapStore> {
    protected abstract void onMapPressed(View v, MapStore store);

    private static final int LAYOUT_ID = R.layout.map_packs_list_row;

    public AbstractMapsAdapter(Context context, List<MapStore> stores) {
        super(context, LAYOUT_ID, stores);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Typeface tf = UiUtils.getTextFont(getContext());
        final MapStore store = getItem(position);

        Button buttonTag;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(LAYOUT_ID, null);

            buttonTag = (Button)convertView.findViewById(R.id.map_pack_name);
            buttonTag.setTypeface(tf);
            convertView.setTag(buttonTag);
        }
        else {
            buttonTag = (Button)convertView.getTag();
        }
        if (store != null) {
            buttonTag.setText(store.getName());
            buttonTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { onMapPressed(v, store); }
            });
        }
        return convertView;
    }
}
