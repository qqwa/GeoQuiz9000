package no.ntnu.tdt4240.geoquiz9000.activities;

import android.content.Context;
import android.content.Intent;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        m_questionText = (TextView)findViewById(R.id.question_text);
        m_questionText.setTypeface(getTextFont());

        m_questionPic = (ImageView)findViewById(R.id.question_pic);

        Button mapBtn = (Button)findViewById(R.id.map_btn);
        mapBtn.setTypeface(getTextFont());
        mapBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(MapsActivity.newIntent(QuestionActivity.this), REQUEST_MAPS);
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_MAPS:
                double distance = MapsActivity.getDistance(data);
                // TODO: 20.03.2017
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
