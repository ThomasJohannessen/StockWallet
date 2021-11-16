package no.stockwallet.Fragments.Wrappers;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.HashMap;

import no.stockwallet.MainActivity;
import no.stockwallet.R;
import yahoofinance.Stock;

public class DetailStockFragmentsWrapper extends Fragment {

    public DetailStockFragmentsWrapper() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle bundle = getArguments();
        HashMap<String, Stock> map = (HashMap<String, Stock>) bundle.getSerializable("hashmap");
        ((MainActivity)getActivity()).setToolbarTitle(map.get("Stock").getSymbol());

        Log.d("MAP", map.toString());
        return inflater.inflate(R.layout.fragment_detail_stock_fragments_wrapper, container, false);
    }
}