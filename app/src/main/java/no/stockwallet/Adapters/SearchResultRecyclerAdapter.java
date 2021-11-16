package no.stockwallet.Adapters;

import android.app.Activity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import no.stockwallet.Fragments.SearchFragments.SearchHistoryFragment;
import no.stockwallet.Fragments.Wrappers.DetailStockFragmentsWrapper;
import no.stockwallet.Handlers.StockDataRetriever;
import no.stockwallet.Model.Investment;
import no.stockwallet.R;
import yahoofinance.Stock;

public class SearchResultRecyclerAdapter extends RecyclerView.Adapter<SearchResultRecyclerAdapter.ViewHolder>{
    private ArrayList<Pair<String, String>> data;
    private View parent;

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

    public SearchResultRecyclerAdapter(View parent) {
        data = new ArrayList<>();
        this.parent = parent;
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

        setUpClickListener(holder, stock.first);
    }

    private void setUpClickListener(ViewHolder holder, String stockTicker) {
        holder.itemView.setOnClickListener(view -> {
            HashMap<String, Stock> stock = new HashMap<>();
            StockDataRetriever.getInstance().getStockObject(stock, stockTicker);
            Navigation.findNavController(view).navigate(R.id.detailStockFragmentsWrapper);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(ArrayList<Pair<String, String>> data) {
        this.data = data;
    }
}
