package no.stockwallet.Fragments.BuySellFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import no.stockwallet.Fragments.Wrappers.BuySellFragmentsWrapper;
import no.stockwallet.Model.Investment;
import no.stockwallet.R;

public class BuySellFragment extends Fragment {
    private View fragmentView;

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
        fragmentView = view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpButtonListeners(fragmentView);
    }

    private void setUpButtonListeners(View view) {
        Button buySellButton = getActivity().findViewById(R.id.RegisterBuySellButton);
        buySellButton.setOnClickListener(view1 -> handleRegisterClick(view));
    }

    private void handleRegisterClick(View view) {
        try {
            EditText tickerName = view.findViewById(R.id.InputStockName);
            String ticker = tickerName.getText().toString().toUpperCase(Locale.ROOT);
            if (ticker.equals(""))
                throw new NullPointerException("Tickername");

            EditText stockPrice = view.findViewById(R.id.InputStockPrice);
            if(stockPrice.getText().toString().equals(""))
                throw new NullPointerException("Stockprice");
            float price = Math.round(Float.parseFloat(stockPrice.getText().toString()) * 100) / 100;

            EditText stockVolume = view.findViewById(R.id.InputStockVolume);
            if(stockVolume.getText().toString().equals(""))
                throw new NullPointerException("Stockvolume");
            int volume = Integer.parseInt(stockVolume.getText().toString());

            EditText stockFee = view.findViewById(R.id.InputStockFee);
            if(stockFee.getText().toString().equals(""))
                throw new NullPointerException("StockFee");
            float fee = Math.round(Float.parseFloat(stockFee.getText().toString()) * 100) / 100;

            BuySellFragmentsWrapper parent = (BuySellFragmentsWrapper) getParentFragment();

            Investment newInvestment = new Investment(ticker, volume, price, fee);
            Spinner dropdown = view.findViewById(R.id.BuySellDropdown);
            String selected = dropdown.getSelectedItem().toString();

            switch (selected) {
                case "Kjøp":
                    parent.purchaseStock(newInvestment);
                    break;
                case "Salg":
                    parent.sellStock(newInvestment);
                    break;
                default:
                    Snackbar.make(view, "Something went wrong when handling the request", Snackbar.LENGTH_SHORT);
                    break;
            }

            try {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Navigation.findNavController(view).navigate(R.id.overviewFragmentsWrapper);
        }
        catch(NullPointerException e) {
            Snackbar.make(view, e.getMessage() + " can't be empty", Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        catch(NumberFormatException e) {
            Snackbar.make(view, "Something went wrong when parsing input. Check if every field is correct", Snackbar.LENGTH_SHORT).show();
        }
    }
}