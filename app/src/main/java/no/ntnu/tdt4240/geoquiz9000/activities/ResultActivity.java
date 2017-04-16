package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.models.Score;
import no.ntnu.tdt4240.geoquiz9000.ui.ResultsArrayAdapter;
import no.ntnu.tdt4240.geoquiz9000.ui.UiUtils;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = ResultActivity.class.getSimpleName();
    public static final String INTENT_SCORE = "intent score";

    private ArrayList<Score> mScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mScores = getIntent().getParcelableArrayListExtra(INTENT_SCORE);
        saveScores(mScores);

        TextView titleTv = (TextView) findViewById(R.id.result_title);
        titleTv.setTypeface(UiUtils.getTitleFont(this));

        ResultsArrayAdapter adapter = new ResultsArrayAdapter(this, R.layout.result_list_row,
                mScores);

        ListView resultsList = (ListView) findViewById(R.id.results_list_view);
        resultsList.setAdapter(adapter);

        Button tryAgainBtn = (Button) findViewById(R.id.try_again_btn);
        tryAgainBtn.setTypeface(UiUtils.getTextFont(this));
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = MenuActivity.startMapChooserIntent(getApplicationContext(), mScores.size());
                startActivity(i);
                finish();
            }
        });

        Button quitBtn = (Button) findViewById(R.id.quit_btn);
        quitBtn.setTypeface(UiUtils.getTextFont(this));
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public static Intent newIntent(Context context, ArrayList<Score> scores) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putParcelableArrayListExtra(INTENT_SCORE, scores);
        return intent;
    }

    /**
     * Save all scores.
     *
     * @param scoreList List of all scores.
     */
    private void saveScores(List<Score> scoreList) {
        for (Score score : scoreList) {
            score.save(this);
        }
    }

}
