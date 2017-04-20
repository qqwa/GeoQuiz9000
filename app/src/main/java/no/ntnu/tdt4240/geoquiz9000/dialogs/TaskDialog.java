package no.ntnu.tdt4240.geoquiz9000.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.activities.GeoActivity;


public class TaskDialog extends DialogFragment {
    public interface Callbacks {
        void onCancelPressed();
        void onOkPressed();
    }

    public static TaskDialog newInstance() {
        TaskDialog dialog = new TaskDialog();
        return dialog;
    }

    private Callbacks m_callbacks;
    private TextView m_taskLog;
    private boolean m_canDismiss = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        m_callbacks = (Callbacks)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        m_callbacks = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Typeface font = ((GeoActivity)getActivity()).getTextFont();

        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_task, null);
        TextView taskLog = (TextView)root.findViewById(R.id.taskLog);
        m_taskLog = taskLog;
        taskLog.setTypeface(font);
        taskLog.setText("Initiating Task...");

        Dialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("")
                .setView(root)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button ok = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (m_callbacks!=null)
                            m_callbacks.onOkPressed();
                        if(m_canDismiss) {
                            dialog.dismiss();
                        }
                    }
                });

                Button cancel = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (m_callbacks != null) {
                            m_callbacks.onCancelPressed();
                            if(m_canDismiss) {
                                dialog.dismiss();
                            }
                        }
                    }
                });
            }
        });

        return dialog;
    }

    public void setTaskLog(String log) {
        m_taskLog.setText(log);
    }

    public void setCanDismiss(boolean dismiss) {
        setCancelable(true);
        m_canDismiss = dismiss;
    }
}
