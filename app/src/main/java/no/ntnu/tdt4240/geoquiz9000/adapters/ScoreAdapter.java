package no.ntnu.tdt4240.geoquiz9000.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.models.Score;
import no.ntnu.tdt4240.geoquiz9000.ui.UiUtils;
import no.ntnu.tdt4240.geoquiz9000.utils.GeoUtils;


public class ScoreAdapter extends ArrayAdapter<Score>
{
    private static final int LAYOUT_ID = R.layout.item_score;

    public ScoreAdapter(Context context, List<Score> scores)
    {
        super(context, LAYOUT_ID, scores);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        final Score record = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(LAYOUT_ID, null);
            holder = new ViewHolder(convertView, getContext());
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        if (record != null) {
            holder.bind(record);
        }
        return convertView;
    }

    private static class ViewHolder
    {
        private final TextView m_playerName;
        private final TextView m_distance;
        private final TextView m_mapName;

        public ViewHolder(View rootView, Context context)
        {
            Typeface font = UiUtils.getTextFont(context);
            m_playerName = (TextView)rootView.findViewById(R.id.player_label);
            m_playerName.setTypeface(font);
            m_distance = (TextView)rootView.findViewById(R.id.total_distance_label);
            m_distance.setTypeface(font);
            m_mapName = (TextView)rootView.findViewById(R.id.map_pack_label);
            m_mapName.setTypeface(font);
        }
        public void bind(Score data)
        {
            m_playerName.setText(data.getPlayerName());
            m_distance.setText(data.getTotalDistance() + GeoUtils.getCurrentUnits().toString());
            m_mapName.setText(data.getMapPackName());
        }
    }
}
