package no.ntnu.tdt4240.geoquiz9000.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * Created by MikhailV on 04.04.2017.
 */

public class SingleplayerFragment extends Fragment
{
    public interface Callbacks
    {
        void onPlacePinPressed();

        void onQuit(int currentPicId);
    }

    public static SingleplayerFragment newInstance(int pictureId)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PIC_ID, pictureId);

        SingleplayerFragment f = new SingleplayerFragment();
        f.setArguments(args);
        return f;
    }
    private static final String ARG_PIC_ID = "SingleplayerFragment.ARG_PIC_ID";

    private TextView m_questionText;
    private ImageView m_questionPic;
    private Button m_mapBtn;
    private Button m_quitBtn;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        Typeface font = ((GeoActivity)getActivity()).getTextFont();
        final int pictureId = getArguments().getInt(ARG_PIC_ID);

        View root = inflater.inflate(R.layout.fragment_question, container, false);

        m_questionPic = (ImageView)root.findViewById(R.id.question_pic);

        m_questionText = (TextView)root.findViewById(R.id.question_text);
        m_questionText.setTypeface(font);

        m_quitBtn = (Button)root.findViewById(R.id.quit_btn);
        m_quitBtn.setTypeface(font);
        m_quitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_callbacks != null)
                    m_callbacks.onQuit(pictureId);
            }
        });

        m_mapBtn = (Button)root.findViewById(R.id.map_btn);
        m_mapBtn.setTypeface(font);
        m_mapBtn.setText(R.string.map_btn_label);
        m_mapBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (m_callbacks != null)
                    m_callbacks.onPlacePinPressed();
            }
        });

        try {
            Box maps = DatabaseLayer.getInstance(getActivity()).getBoxFor(MapStore.class);
            //load imported map
            if (maps.find("name", "Test Map Pack").size() != 0) {
                MapStore testMap = (MapStore)maps.find("name", "Test Map Pack").get(0);
                MapGoogle mapGoogle = (MapGoogle)MapFactory.getMap(testMap);
                m_questionPic.setImageBitmap(mapGoogle.getLocationPicture(pictureId));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return root;
    }
}
