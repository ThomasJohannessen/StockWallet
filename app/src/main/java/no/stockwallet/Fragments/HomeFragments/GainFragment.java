package no.stockwallet.Fragments.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import no.stockwallet.Investment;
import no.stockwallet.MainActivity;
import no.stockwallet.R;

public class GainFragment extends Fragment {
    private TextView tempView, tempView2, tempView3, tempView4, tempView5, tempView6;


    public GainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gain, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Investment> data = new ArrayList<>(MainActivity.investments.values());


        tempView = view.findViewById(R.id.stockNameR1);
        tempView2 = view.findViewById(R.id.stockPCR1);
        tempView3 = view.findViewById(R.id.stockPriceR1);

        tempView4 = view.findViewById(R.id.stockNameR4);
        tempView5 = view.findViewById(R.id.stockPCR4);
        tempView6 = view.findViewById(R.id.stockPriceR4);

        tempView.setText(data.get(0).getTicker());
        tempView2.setText("+20%");
        tempView3.setText(String.valueOf(data.get(0).getPrice()) + " " + data.get(0).getCurrency());

        tempView4.setText(data.get(2).getTicker());
        tempView5.setText("-25%");
        tempView6.setText(String.valueOf(data.get(2).getPrice()) + " " + data.get(2).getCurrency());
    }
}