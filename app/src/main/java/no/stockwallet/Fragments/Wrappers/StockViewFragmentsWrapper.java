package no.stockwallet.Fragments.Wrappers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.stockwallet.MainActivity;
import no.stockwallet.R;
import no.stockwallet.ViewModels.StockViewModel;

public class StockViewFragmentsWrapper extends Fragment {

    private StockViewModel viewModel;

    public StockViewFragmentsWrapper() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).setToolbarTitle("Aksje");
        viewModel = new ViewModelProvider(requireActivity()).get(StockViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_view_fragments_wrapper, container, false);
    }
}