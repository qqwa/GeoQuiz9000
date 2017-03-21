package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import no.ntnu.tdt4240.geoquiz9000.R;

/**
 * Created by MikhailV on 21.03.2017.
 */

public class ScoreActivity extends GeoActivity
{
    public static Intent newIntent(Context context)
    {
        return new Intent(context, ScoreActivity.class);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView title = (TextView)findViewById(R.id.score_title);
        title.setTypeface(getTitleFont());

        RecyclerView list = (RecyclerView)findViewById(R.id.score_list);
        list.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        list.setAdapter(new ScoreAdapter(this));
    }

    private static class ScoreHolder extends RecyclerView.ViewHolder
    {
        final private TextView m_name;
        final private TextView m_score;

        public ScoreHolder(View root, Typeface textFont)
        {
            super(root);
            m_name = (TextView)root.findViewById(R.id.player_label);
            m_name.setTypeface(textFont);
            m_score = (TextView)root.findViewById(R.id.score_label);
            m_score.setTypeface(textFont);
        }
        public void bind(String playerName, int highScore)
        {
            m_name.setText(playerName);
            m_score.setText(highScore + "");
        }
    }

    private static class ScoreAdapter extends RecyclerView.Adapter<ScoreHolder>
    {
        private final LayoutInflater m_inflater;
        private final ScoreActivity m_activity;

        public ScoreAdapter(ScoreActivity activity)
        {
            m_activity = activity;
            m_inflater = LayoutInflater.from(activity);
        }
        @Override
        public ScoreHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View itemView = m_inflater.inflate(R.layout.item_score, parent, false);
            return new ScoreHolder(itemView, m_activity.getTextFont());
        }
        @Override
        public void onBindViewHolder(ScoreHolder holder, int position)
        {
            // TODO: 21.03.2017 retrieve score from DB and then call holder.bind()
        }
        @Override
        public int getItemCount()
        {
            // TODO: 21.03.2017 retrieve the total number of highscores from DB
            return 0;
        }
    }
}
