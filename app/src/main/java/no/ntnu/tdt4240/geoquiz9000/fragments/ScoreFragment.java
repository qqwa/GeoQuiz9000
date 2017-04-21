package no.ntnu.tdt4240.geoquiz9000.fragments;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import io.objectbox.Box;
import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.adapters.ScoreAdapter;
import no.ntnu.tdt4240.geoquiz9000.database.DatabaseLayer;
import no.ntnu.tdt4240.geoquiz9000.models.Score;


public class ScoreFragment extends AbstractListFragment<Score>
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
    protected ArrayAdapter<Score> getAdapter()
    {
        Box scores = DatabaseLayer.getInstance(getActivity()).getBoxFor(Score.class);
        List<Score> allScores = scores.getAll();
        return new ScoreAdapter(getContext(), allScores);
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
