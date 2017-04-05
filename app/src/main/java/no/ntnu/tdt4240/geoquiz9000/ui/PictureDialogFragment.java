package no.ntnu.tdt4240.geoquiz9000.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import no.ntnu.tdt4240.geoquiz9000.R;

public class PictureDialogFragment extends DialogFragment {

    public static final String NR_OF_QUESTION = "nr of question";
    public static final String IMAGE_PATH = "image path";

    private int mQuestionNr;
    private String mImagePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mQuestionNr = getArguments().getInt(NR_OF_QUESTION);
        mImagePath = getArguments().getString(IMAGE_PATH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.picture_dialog, container, false);

        String titleText = "Question: " + String.valueOf(mQuestionNr + 1);

        TextView title = (TextView) v.findViewById(R.id.title);
        title.setText(titleText);

        ImageView iv = (ImageView) v.findViewById(R.id.question_picture);
        Bitmap pic = BitmapFactory.decodeFile(mImagePath);
        iv.setImageBitmap(pic);

        TextView answerButton = (TextView) v.findViewById(R.id.answer_button);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }
}
