package no.ntnu.tdt4240.geoquiz9000.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.models.Score;
import no.ntnu.tdt4240.geoquiz9000.ui.UiUtils;
import no.ntnu.tdt4240.geoquiz9000.utils.GeoUtils;

public class ResultsArrayAdapter extends ArrayAdapter<Score> {
    private ArrayList<Score> scores;
    private int layout;

    public ResultsArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Score> scores) {
        super(context, resource, scores);
        this.scores = scores;
        Collections.sort(this.scores);
        this.layout = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Typeface tf = UiUtils.getTextFont(getContext());
        Score score = scores.get(position);

        ViewHolder holder = new ViewHolder();
        View view = convertView;

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(layout, null);

            holder.ranking = (TextView) view.findViewById(R.id.text_ranking);
            holder.ranking.setTypeface(tf);

            holder.playerName = (TextView) view.findViewById(R.id.text_player_name);
            holder.playerName.setTypeface(tf);

            holder.result = (TextView) view.findViewById(R.id.text_result);
            holder.result.setTypeface(tf);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        String ranking = String.valueOf(position + 1) + ".";
        holder.ranking.setText(ranking);

        holder.playerName.setText(score.getPlayerName());

        // Meter representation of result to km
        float distance =  score.getTotalDistance() / 1000;
        switch (GeoUtils.getCurrentUnits(getContext()))
        {
            case MILES:
                distance = GeoUtils.kmToMiles(distance);
                break;
            default:
                break;
        }


        String result = String.format("%.02f", distance) + GeoUtils.getCurrentUnits(getContext()).toString();
        holder.result.setText(result);

        return view;
    }

    private static class ViewHolder {
        TextView ranking;
        TextView playerName;
        TextView result;
    }
}
