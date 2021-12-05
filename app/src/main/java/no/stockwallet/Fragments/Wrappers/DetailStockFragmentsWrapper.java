package no.stockwallet.Fragments.Wrappers;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Objects;

import no.stockwallet.MainActivity;
import no.stockwallet.R;
import no.stockwallet.ViewModels.StockViewModel;
import yahoofinance.Stock;

public class DetailStockFragmentsWrapper extends Fragment {

    private StockViewModel viewModel;

    public StockViewModel getViewModel(){
        return viewModel;
    }


    public DetailStockFragmentsWrapper() {
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
        hideKeyboard();
        return inflater.inflate(R.layout.fragment_detail_stock_fragments_wrapper, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        setToolbarText(view, arguments);
    }

    private void setToolbarText(View view, Bundle arguments) {
        HashMap<String, Stock> map = (HashMap<String, Stock>) arguments.getSerializable("hashmap");
        Stock stock = map.get("Stock");
        try {
            if(stock.getName().length() > 50)
                throw new Exception();
            ((MainActivity)getActivity()).setToolbarTitle(stock.getName());
        }
        catch (NullPointerException e) {
            ((MainActivity)getActivity()).setToolbarTitle("Aksje");
            Snackbar.make(view, "Error communicating with the server, some data may be missing", Snackbar.LENGTH_LONG).show();
        }
        catch (Exception e) {
            ((MainActivity) requireActivity()).setToolbarTitle(stock.getSymbol());
        }
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