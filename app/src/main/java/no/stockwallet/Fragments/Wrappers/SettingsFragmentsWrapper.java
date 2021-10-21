package no.stockwallet.Fragments.Wrappers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.stockwallet.MainActivity;
import no.stockwallet.R;
import no.stockwallet.StockViewModel;

public class SettingsFragmentsWrapper extends Fragment {
    private StockViewModel viewModel;

    public SettingsFragmentsWrapper() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).setToolbarTitle("Innstillinger");
        viewModel = new ViewModelProvider(this).get(StockViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_fragments_wrapper, container, false);
    }
}