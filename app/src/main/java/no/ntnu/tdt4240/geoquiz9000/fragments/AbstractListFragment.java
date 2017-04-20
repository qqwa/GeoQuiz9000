package no.ntnu.tdt4240.geoquiz9000.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.activities.GeoActivity;


public abstract class AbstractListFragment<TItem> extends ListFragment
{

    protected abstract ArrayAdapter<TItem> getAdapter();

    protected abstract void onBackPressed();

    protected abstract CharSequence getListLabel();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setListAdapter(getAdapter());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Typeface font = ((GeoActivity)getActivity()).getTextFont();
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        Button backBtn = (Button)root.findViewById(R.id.back_btn);
        backBtn.setTypeface(font);
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) { onBackPressed(); }
        });
        TextView info = (TextView)root.findViewById(R.id.list_info);
        info.setTypeface(font);
        info.setText(getListLabel());
        return root;
    }
}
