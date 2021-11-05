package no.stockwallet.Fragments.BuySellFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import no.stockwallet.Fragments.Wrappers.BuySellFragmentsWrapper;
import no.stockwallet.Model.Investment;
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

        setUpButtonListeners();
    }

    private void setUpButtonListeners() {
        Button buySellButton = getActivity().findViewById(R.id.RegisterBuySellButton);
        buySellButton.setOnClickListener(view -> handleRegisterClick(view));
    }

    private void handleRegisterClick(View view) {

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
            Investment newInvestment = new Investment(ticker, volume, price, currency, fee);
            parent.registerStockSale(newInvestment);

            try {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Navigation.findNavController(view).navigate(R.id.overviewFragmentsWrapper);
    }
}