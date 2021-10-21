package no.stockwallet.Fragments.OverviewFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.stockwallet.Fragments.Wrappers.OverviewFragmentsWrapper;
import no.stockwallet.R;


public class StockListFragment extends Fragment {

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OverviewFragmentsWrapper parent = (OverviewFragmentsWrapper) getParentFragment();

        RecyclerView stockRecyclerView = view.findViewById(R.id.OverviewRecyclerView);
        stockRecyclerView.setAdapter(parent.getAdapter());
        stockRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 1));

    }

}