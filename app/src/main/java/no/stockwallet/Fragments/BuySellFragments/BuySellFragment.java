package no.stockwallet.Fragments.BuySellFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

import no.stockwallet.Fragments.Wrappers.BuySellFragmentsWrapper;
import no.stockwallet.Investment;
import no.stockwallet.R;

public class BuySellFragment extends Fragment {

    public BuySellFragment() {
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
        return inflater.inflate(R.layout.fragment_buy_sell, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buySellButton = getActivity().findViewById(R.id.RegisterBuySellButton);
        buySellButton.setOnClickListener(view1 -> {
            EditText tickerName = view.findViewById(R.id.InputStockName);
            String ticker = tickerName.getText().toString();

            EditText stockPrice = view.findViewById(R.id.InputStockPrice);
            float price = Math.round(Float.parseFloat(stockPrice.getText().toString()) * 100) / 100;

            EditText stockVolume = view.findViewById(R.id.InputStockVolume);
            int volume = Integer.parseInt(stockVolume.getText().toString());

            EditText stockExchange = view.findViewById(R.id.InputStockExchange);
            String exchange = stockExchange.getText().toString();

            EditText stockCurrency = view.findViewById(R.id.InputStockCurrency);
            String currency = stockCurrency.getText().toString();

            EditText stockFee = view.findViewById(R.id.InputStockFee);
            float fee = Math.round(Float.parseFloat(stockFee.getText().toString()) * 100) / 100;

            EditText stockDate = view.findViewById(R.id.InputStockDate);
            String date = stockDate.getText().toString();

            BuySellFragmentsWrapper parent = (BuySellFragmentsWrapper) getParentFragment();
            parent.registerStockSale(ticker, volume, price,  currency, fee);
        });
    }
}