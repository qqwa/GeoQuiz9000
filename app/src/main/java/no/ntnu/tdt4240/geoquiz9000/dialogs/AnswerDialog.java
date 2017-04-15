package no.ntnu.tdt4240.geoquiz9000.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.objectbox.Box;
import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.activities.GeoActivity;
import no.ntnu.tdt4240.geoquiz9000.controllers.MapFactory;
import no.ntnu.tdt4240.geoquiz9000.database.DatabaseLayer;
import no.ntnu.tdt4240.geoquiz9000.models.MapGoogle;
import no.ntnu.tdt4240.geoquiz9000.models.MapStore;

/**
 * To be started upon returning from MapsActivity back to the QuestionActivity
 */
public class AnswerDialog extends DialogFragment
{
    public interface Callbacks
    {
        void onNextQuestionPressed(int currentQuestionId);
    }
    public static AnswerDialog newInstance(int picId, float distanceError, int pointsEarned)
    {
        Bundle args = new Bundle();
        args.putInt(ARGS_PIC_ID, picId);
        args.putFloat(ARGS_DISTANCE, distanceError);
        args.putInt(ARGS_POINTS, pointsEarned);
        AnswerDialog dialog = new AnswerDialog();
        dialog.setArguments(args);
        return dialog;
    }
    private static final String ARGS_PIC_ID = "AnswerDialog.ARGS_PIC_ID";
    private static final String ARGS_DISTANCE = "AnswerDialog.ARGS_DISTANCE";
    private static final String ARGS_POINTS = "AnswerDialog.ARGS_POINTS";

    private Callbacks m_callbacks;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        m_callbacks = (Callbacks)context;
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        m_callbacks = null;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Typeface font = ((GeoActivity)getActivity()).getTextFont();
        final int pictureId = getArguments().getInt(ARGS_PIC_ID);
        int score = getArguments().getInt(ARGS_POINTS);
        float distance = getArguments().getFloat(ARGS_DISTANCE);

        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_answer, null);

        ImageView pic = (ImageView)root.findViewById(R.id.question_pic);
        try {
            Box maps = DatabaseLayer.getInstance(getActivity()).getBoxFor(MapStore.class);
            //load imported map
            if (maps.find("name", "Test Map Pack").size() != 0) {
                MapStore testMap = (MapStore)maps.find("name", "Test Map Pack").get(0);
                MapGoogle mapGoogle = (MapGoogle)MapFactory.getMap(testMap);
                pic.setImageBitmap(mapGoogle.getLocationPicture(pictureId));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        TextView msg = (TextView)root.findViewById(R.id.answer_msg);
        msg.setTypeface(font);
        msg.setText(getString(R.string.question_result, distance, score));

        return new AlertDialog.Builder(getContext())
                .setTitle("")
                .setView(root)
                .setPositiveButton(R.string.next_question_label, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (m_callbacks != null)
                            m_callbacks.onNextQuestionPressed(pictureId);
                    }
                })
                .create();
    }
}
