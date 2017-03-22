package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import no.ntnu.tdt4240.geoquiz9000.R;

/**
 * Created by MikhailV on 20.03.2017.
 */

public class QuestionActivity extends GeoActivity
{
    public static Intent newIntent(Context context)
    {
        return new Intent(context, QuestionActivity.class);
    }

    private static final int REQUEST_MAPS = 0;

    private TextView m_questionText;
    private ImageView m_questionPic;
    private Button m_mapBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        m_questionText = (TextView)findViewById(R.id.question_text);
        m_questionText.setTypeface(getTextFont());
        m_mapBtn = (Button)findViewById(R.id.map_btn);
        m_mapBtn.setTypeface(getTextFont());
        m_questionPic = (ImageView)findViewById(R.id.question_pic);

        // TODO: 21.03.2017 retrieve question from DB, set question text, set question pic:
        updateViews(null, null);    // temp
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_MAPS:
                float distance = MapsActivity.getDistance(data);
                int score = (int)(1f / distance); // TODO: 21.03.2017 calculate score
                final String resultMsg = getString(R.string.question_result, distance, score);

                m_questionText.setText(resultMsg);
                m_mapBtn.setText(R.string.map_btn_label1);
                m_mapBtn.setOnClickListener(new NextQuestionClick());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void updateViews(String questionText, Bitmap questionImage)
    {
        m_mapBtn.setText(R.string.map_btn_label0);
        m_mapBtn.setOnClickListener(new PlacePinClick());
        m_questionText.setText(questionText);
        m_questionPic.setImageBitmap(questionImage);
    }

    private class PlacePinClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            // TODO: 21.03.2017 pass arguments (target coordinates) to the MapsActivity through newIntent()
            startActivityForResult(MapsActivity.newIntent(QuestionActivity.this), REQUEST_MAPS);
        }
    }

    private class NextQuestionClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            // TODO: 21.03.2017 retrieve the next question from DB and update views:
            updateViews(null, null);    // temp
        }
    }
}
