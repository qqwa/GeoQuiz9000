package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.models.Score;
import no.ntnu.tdt4240.geoquiz9000.ui.UiUtils;

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

        TextView titleTv = (TextView) findViewById(R.id.result_title);
        titleTv.setTypeface(UiUtils.getTitleFont(this));

        TextView tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setTypeface(UiUtils.getTextFont(this));

        // Meter representation of result to km
        String result = String.format("%.02f", mScore.getTotalDistance() / 1000) + "km";
        TextView resultTv = (TextView) findViewById(R.id.result_tv);
        resultTv.setTypeface(UiUtils.getTextFont(this));
        resultTv.setText(result);

        Button tryAgainBtn = (Button) findViewById(R.id.try_again_btn);
        tryAgainBtn.setTypeface(UiUtils.getTextFont(this));
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO go to MapChooserFragment
                startActivity(MapsActivity
                        .newIntent(getApplicationContext(), mScore.getMapPackName(), 1));
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

    public static Intent newIntent(Context context, Score score) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra(INTENT_SCORE, score);
        return intent;
    }

}
