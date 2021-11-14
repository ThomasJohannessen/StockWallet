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
        private TextView tickerTextView;
        private TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tickerTextView = itemView.findViewById(R.id.dummyName);
            nameTextView = itemView.findViewById(R.id.dummyValue);
        }

        public TextView getTickerTextView() {return tickerTextView;}
        public TextView getNameTextView() {return nameTextView;}
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
        Pair<String, String> stock = data.get(position);
        holder.getTickerTextView().setText(stock.first);
        holder.getNameTextView().setText(stock.second);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(ArrayList<Pair<String, String>> data) {
        this.data = data;
    }
}
