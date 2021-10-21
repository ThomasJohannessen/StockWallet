package no.stockwallet.Fragments.Wrappers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import no.stockwallet.Investment;
import no.stockwallet.MainActivity;
import no.stockwallet.R;
import no.stockwallet.StockViewModel;

public class OverviewFragmentsWrapper extends Fragment {
    private StockViewModel viewModel;

    public OverviewFragmentsWrapper() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).setToolbarTitle("Oversikt");
        viewModel = new ViewModelProvider(this).get(StockViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview_fragments_wrapper, container, false);
    }

    public HashMap<String, Investment> getData() {
        viewModel.fillWithDummyData();
        return viewModel.getStockMap();
    }

    public StockViewModel getViewModel() {
        return viewModel;
    }
}