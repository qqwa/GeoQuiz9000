package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.models.Score;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = ResultActivity.class.getSimpleName();
    public static final String INTENT_SCORE = "intent score";

    private Score mScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mScore = getIntent().getParcelableExtra(INTENT_SCORE);
        String result = String.format("%.02f", mScore.getTotalDistance() / 1000) + "km";

        TextView resultTv = (TextView) findViewById(R.id.result_tv);
        resultTv.setText(result);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, ResultActivity.class);
    }

}
