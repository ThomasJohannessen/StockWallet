package no.stockwallet.Fragments.SearchFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.stockwallet.Adapters.SearchResultRecyclerAdapter;
import no.stockwallet.R;

public class SearchHistoryFragment extends Fragment {

    public SearchHistoryFragment() {
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
        return inflater.inflate(R.layout.fragment_search_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView(view);
    }

    private void setUpRecyclerView(View view) {
        RecyclerView searchRecycler = view.findViewById(R.id.searchRecyclerView);
        searchRecycler.setAdapter(new SearchResultRecyclerAdapter());
        searchRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}