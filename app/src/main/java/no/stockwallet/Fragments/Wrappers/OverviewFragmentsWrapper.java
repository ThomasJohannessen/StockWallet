package no.stockwallet.Fragments.Wrappers;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.system.ErrnoException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.HashMap;
import java.util.Objects;

import no.stockwallet.Model.Investment;
import no.stockwallet.MainActivity;
import no.stockwallet.R;
import no.stockwallet.ViewModels.StockViewModel;

public class OverviewFragmentsWrapper extends Fragment {
    public StockViewModel viewModel;

    public OverviewFragmentsWrapper() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(StockViewModel.class);

        try {
            ((MainActivity)getActivity()).setToolbarTitle("Oversikt");

        }catch (NullPointerException e){
            ((MainActivity) requireActivity()).setToolbarTitle("Oversikt");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        hideKeyboard();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview_fragments_wrapper, container, false);
    }

    public HashMap<String, Investment> getData() {
        return viewModel.getStockMap().getValue();
    }

    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}