package no.ntnu.tdt4240.geoquiz9000.fragments;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.activities.GeoActivity;

/**
 * Created by MikhailV on 04.04.2017.
 */

public class ScoreFragment extends AbstractListFragment<String>
{
    public interface Callbacks
    {
        void onScoreBackPressed();
    }

    private Callbacks m_callbacks;

    @Override
    protected CharSequence getListLabel()
    {
        return getResources().getString(R.string.score_info_label);
    }
    @Override
    protected ArrayAdapter<String> getAdapter()
    {
        return new ArrayAdapter<String>(getContext(), 0, new ArrayList<String>()); // TODO: retrieve scores
    }
    @Override
    protected void onBackPressed()
    {
        if (m_callbacks != null)
            m_callbacks.onScoreBackPressed();
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        m_callbacks = (Callbacks)context;
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        m_callbacks = null;
    }

}
