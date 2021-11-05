package no.stockwallet.Fragments.OverviewFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import no.stockwallet.Adapters.StockRecyclerAdapter;
import no.stockwallet.Fragments.Wrappers.OverviewFragmentsWrapper;
import no.stockwallet.Model.Investment;
import no.stockwallet.R;


public class StockListFragment extends Fragment {
    private StockRecyclerAdapter adapter;
    public StockListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock_list, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observer<HashMap<String, Investment>> observer = stringInvestmentHashMap -> adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView(view);
    }

    private void setUpRecyclerView(View view) {
        OverviewFragmentsWrapper parent = (OverviewFragmentsWrapper) getParentFragment();
        RecyclerView stockRecyclerView = view.findViewById(R.id.OverviewRecyclerView);
        adapter = new StockRecyclerAdapter(parent.getData());
        stockRecyclerView.setAdapter(adapter);
        stockRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
    }

}