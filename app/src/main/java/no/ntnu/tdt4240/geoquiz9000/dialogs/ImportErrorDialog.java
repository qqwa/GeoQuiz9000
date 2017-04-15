package no.ntnu.tdt4240.geoquiz9000.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import no.ntnu.tdt4240.geoquiz9000.R;

public class ImportErrorDialog extends DialogFragment
{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.import_error_msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
                })
                .create();
    }
}
