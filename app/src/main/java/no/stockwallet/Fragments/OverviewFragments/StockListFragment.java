package no.stockwallet.Fragments.OverviewFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.stockwallet.R;


public class StockListFragment extends Fragment {
    public StockListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView stockRecyclerView = view.findViewById(R.id.OverviewRecyclerView);
        stockRecyclerView.setAdapter(new StockRecyclerAdapter());
        stockRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}