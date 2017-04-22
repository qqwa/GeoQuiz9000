package no.ntnu.tdt4240.geoquiz9000.dialogs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.ui.UiUtils;

public class ResultDialog extends DialogFragment {

    public static final String IMAGE_PATH = "image path";
    private String mImagePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mImagePath = getArguments().getString(IMAGE_PATH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Typeface textFont = UiUtils.getTextFont(getContext());

        View v = inflater.inflate(R.layout.dialog_result, container, false);

        TextView title = (TextView)v.findViewById(R.id.result_dialog_title);
        title.setTypeface(textFont);

        TextView content = (TextView)v.findViewById(R.id.result_dialog_content);
        content.setTypeface(textFont);

        ImageView iv = (ImageView)v.findViewById(R.id.result_picture);
        Bitmap pic = BitmapFactory.decodeFile(mImagePath);
        iv.setImageBitmap(pic);

        Button answerButton = (Button)v.findViewById(R.id.result_dialog_btn);
        answerButton.setTypeface(textFont);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }
}
