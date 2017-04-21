package no.ntnu.tdt4240.geoquiz9000.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.ui.UiUtils;

/**
 * Created by MikhailV on 21.04.2017.
 */

public class EnterUrlDialog extends DialogFragment
{
    public interface Callbacks
    {
        void onUrlSubmitted(String url);
    }

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
        Typeface font = UiUtils.getTextFont(getContext());
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_enter_url, null);

        final TextView label = (TextView)root.findViewById(R.id.enter_url_label);
        label.setTypeface(font);

        final EditText editUrl = (EditText)root.findViewById(R.id.url_string);
        editUrl.setTypeface(font);

        return new AlertDialog.Builder(getContext())
                .setView(root)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (m_callbacks != null) {
                            String url = editUrl.getText().toString();
                            m_callbacks.onUrlSubmitted(url);
                        }
                    }
                })
                .create();
    }
}
