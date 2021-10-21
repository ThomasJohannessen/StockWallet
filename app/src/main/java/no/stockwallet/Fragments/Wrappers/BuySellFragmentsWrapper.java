package no.stockwallet.Fragments.Wrappers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import no.stockwallet.BinderSingleton;
import no.stockwallet.Fragments.OverviewFragments.StockListFragment;
import no.stockwallet.Investment;
import no.stockwallet.MainActivity;
import no.stockwallet.R;
import no.stockwallet.StockViewModel;

public class BuySellFragmentsWrapper extends Fragment {
    private StockViewModel viewModel;
    public BuySellFragmentsWrapper() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).setToolbarTitle("Kjøp/Salg");
        viewModel = new ViewModelProvider(requireActivity()).get(StockViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buy_sell_fragments_wrapper, container, false);
    }

    public void registerStockSale(String ticker, int volume, float price, String currency, float brokerage) {
        viewModel.addInvestment(ticker, volume, price, currency, brokerage);
    }
}