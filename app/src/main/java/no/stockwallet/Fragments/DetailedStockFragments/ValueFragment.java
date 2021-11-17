package no.stockwallet.Fragments.DetailedStockFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import no.stockwallet.R;
import yahoofinance.Stock;

public class ValueFragment extends Fragment {

    public ValueFragment() {
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
        return inflater.inflate(R.layout.fragment_value, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle parentArguments = getParentFragment().getArguments();
        HashMap<String, Stock> map = (HashMap<String, Stock>) parentArguments.getSerializable("hashmap");
        Stock stock = map.get("Stock");

        TextView last24View = view.findViewById(R.id.StockLast24PH);
        TextView lastView = view.findViewById(R.id.StockLastPH);

        last24View.setText(String.valueOf(stock.getQuote().getChange() + " %"));
        lastView.setText(String.valueOf(stock.getQuote().getPrice() + " " + stock.getCurrency()));
    }
}