package no.ntnu.tdt4240.geoquiz9000.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.activities.GeoActivity;
import no.ntnu.tdt4240.geoquiz9000.models.IMap;
import no.ntnu.tdt4240.geoquiz9000.models.MapGoogle;
import no.ntnu.tdt4240.geoquiz9000.models.MapPicture;


public abstract class LocationDialog extends DialogFragment
{
    public interface Callbacks
    {
        void onLocationSubmitted(IMap.Location location);

        void onDialogDismissed();
    }

    public static LocationDialog newLocationGoogleDialog()
    {
        return new Google();
    }
    public static LocationDialog newLocationPictureDialog()
    {
        return new Picture();
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
        Typeface font = ((GeoActivity)getActivity()).getTextFont();
        View root = LayoutInflater.from(getContext()).inflate(R.layout.dialog_location, null);

        TextView prompt = (TextView)root.findViewById(R.id.location_prompt);
        prompt.setTypeface(font);

        TextView dim1Label = (TextView)root.findViewById(R.id.dim1_label);
        dim1Label.setTypeface(font);
        dim1Label.setText(getDim1String());

        TextView dim2Label = (TextView)root.findViewById(R.id.dim2_label);
        dim2Label.setTypeface(font);
        dim2Label.setText(getDim2String());


        final EditText dim1edit = (EditText)root.findViewById(R.id.edit_dim1);
        dim1edit.setTypeface(font);
        dim1edit.setInputType(getInputType());

        final EditText dim2edit = (EditText)root.findViewById(R.id.edit_dim2);
        dim2edit.setTypeface(font);
        dim2edit.setInputType(getInputType());

        return new AlertDialog.Builder(getContext())
                .setView(root)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (m_callbacks != null)
                            m_callbacks.onDialogDismissed();
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (m_callbacks != null) {
                            String dim1 = dim1edit.getText().toString();
                            String dim2 = dim2edit.getText().toString();
                            m_callbacks.onLocationSubmitted(getLocation(dim1, dim2));
                        }
                    }
                })
                .create();
    }
    protected abstract int getDim1String();

    protected abstract int getDim2String();

    protected abstract IMap.Location getLocation(String dim1, String dim2);

    protected abstract int getInputType();

    public static class Google extends LocationDialog
    {
        @Override
        protected int getDim1String() { return R.string.longitude; }
        @Override
        protected int getDim2String() { return R.string.latitude; }
        @Override
        protected int getInputType() { return InputType.TYPE_NUMBER_FLAG_DECIMAL; }
        @Override
        protected IMap.Location getLocation(String dim1, String dim2)
        {
            return new MapGoogle.Location(Double.parseDouble(dim1), Double.parseDouble(dim2));
        }
    }

    public static class Picture extends LocationDialog
    {
        @Override
        protected int getDim1String() { return R.string.x; }
        @Override
        protected int getDim2String() { return R.string.y; }
        @Override
        protected int getInputType() { return InputType.TYPE_CLASS_NUMBER; }
        @Override
        protected IMap.Location getLocation(String dim1, String dim2)
        {
            return new MapPicture.Location(Integer.parseInt(dim1), Integer.parseInt(dim2));
        }
    }
}
