package no.stockwallet.Fragments.Wrappers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.stockwallet.Model.Investment;
import no.stockwallet.MainActivity;
import no.stockwallet.R;
import no.stockwallet.ViewModels.StockViewModel;

public class BuySellFragmentsWrapper extends Fragment {
    public StockViewModel viewModel;

    public BuySellFragmentsWrapper() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(StockViewModel.class);

        try {
            ((MainActivity)getActivity()).setToolbarTitle("Kjøp/Salg");

        }catch (NullPointerException e){
            ((MainActivity) requireActivity()).setToolbarTitle("Kjøp/Salg");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buy_sell_fragments_wrapper, container, false);
    }

    public void purchaseStock(Investment investment) {
        viewModel.addInvestment(investment);
    }

    public void sellStock(Investment investment) {
        viewModel.sellInvestment(investment);
    }
}