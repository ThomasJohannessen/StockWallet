package no.stockwallet.Adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import no.stockwallet.Investment;

public class SearchResultRecyclerAdapter extends RecyclerView.Adapter<SearchResultRecyclerAdapter.ViewHolder>{
    private ArrayList<Investment> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public SearchResultRecyclerAdapter() {
        initDummyData();
    }

    private void initDummyData() {
        data = new ArrayList<>();

        data.add(new Investment("NHY.OL",200,60,"NOK",1));
        data.add(new Investment("AKH.OL",100,30,"NOK",10));
        data.add(new Investment("NOD.OL",10,250,"NOK",100));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
