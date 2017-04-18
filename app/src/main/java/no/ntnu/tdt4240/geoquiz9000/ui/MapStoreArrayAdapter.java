package no.ntnu.tdt4240.geoquiz9000.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;

public class MapStoreArrayAdapter extends ArrayAdapter<MapStore> {
    private ArrayList<MapStore> stores;
    private int layout;
    private Context context;

    public MapStoreArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<MapStore> stores) {
        super(context, resource, stores);
        this.stores = stores;
        this.layout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        Typeface tf = UiUtils.getTextFont(getContext());
        final MapStore store = stores.get(position);

        ViewHolder holder = new ViewHolder();
        View view = convertView;
        if(view == null) {
            LayoutInflater vi = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(layout, null);

            holder.name = (Button) view.findViewById(R.id.map_pack_name);
            holder.name.setTypeface(tf);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String name = store.getName();
        holder.name.setText(name);

        final View buttonView = view;

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), buttonView);
                popup.getMenuInflater().inflate(R.menu.menu_map_pack_option, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
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
        });

        return view;
    }

    private static class ViewHolder {
        Button name;
    }
}
