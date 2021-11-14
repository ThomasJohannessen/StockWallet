package no.stockwallet.Adapters;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import no.stockwallet.Model.Investment;
import no.stockwallet.R;

public class SearchResultRecyclerAdapter extends RecyclerView.Adapter<SearchResultRecyclerAdapter.ViewHolder>{
    private ArrayList<Pair<String, String>> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dummy1;
        private TextView dummy2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dummy1 = itemView.findViewById(R.id.dummyName);
            dummy2 = itemView.findViewById(R.id.dummyValue);
        }

        public TextView getDummy1() {return dummy1;}
        public TextView getDummy2() {return dummy2;}
    }

    public SearchResultRecyclerAdapter() {
        data = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(ArrayList<Pair<String, String>> data) {
        this.data = data;
    }
}
