package no.ntnu.tdt4240.geoquiz9000.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import no.ntnu.tdt4240.geoquiz9000.R;
import no.ntnu.tdt4240.geoquiz9000.ui.UiUtils;

public class AddPlayersFragment extends Fragment {

    private TextView mAmountNr;
    private int nrOfPlayers = 2;
    private Callbacks m_callbacks;

    public interface Callbacks {
        void selectMapBtn(int nrOfPlayers);

        void onBackBtnPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        m_callbacks = (Callbacks)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        m_callbacks = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Typeface tf = UiUtils.getTextFont(getContext());

        View v = inflater.inflate(R.layout.fragment_add_players, container, false);

        TextView amountText = (TextView)v.findViewById(R.id.amount_text);
        amountText.setTypeface(tf);

        mAmountNr = (TextView)v.findViewById(R.id.amount_players);
        mAmountNr.setTypeface(tf);
        setAmountNumber(nrOfPlayers);

        Button addPlayerBtn = (Button)v.findViewById(R.id.add_player);
        addPlayerBtn.setTypeface(tf);
        addPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nrOfPlayers++;
                setAmountNumber(nrOfPlayers);
            }
        });

        Button removePlayerBtn = (Button)v.findViewById(R.id.remove_player);
        removePlayerBtn.setTypeface(tf);
        removePlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nrOfPlayers > 2) {
                    nrOfPlayers--;
                    setAmountNumber(nrOfPlayers);
                }
            }
        });

        Button selectMapBtn = (Button)v.findViewById(R.id.choose_map);
        selectMapBtn.setTypeface(tf);
        selectMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m_callbacks != null) {
                    m_callbacks.selectMapBtn(nrOfPlayers);
                }
            }
        });

        Button backBtn = (Button)v.findViewById(R.id.back);
        backBtn.setTypeface(tf);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m_callbacks != null) {
                    m_callbacks.onBackBtnPressed();
                }
            }
        });

        return v;
    }

    private void setAmountNumber(int newNr) {
        mAmountNr.setText(String.valueOf(newNr));
    }
}
