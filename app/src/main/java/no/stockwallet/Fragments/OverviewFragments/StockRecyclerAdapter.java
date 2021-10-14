package no.stockwallet.Fragments.OverviewFragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import no.stockwallet.R;

public class StockRecyclerAdapter extends RecyclerView.Adapter<StockRecyclerAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView stockNameView, stockValueView, stockPercentEarning, stockEarning;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stockNameView = itemView.findViewById(R.id.StockNamePH);
            stockValueView = itemView.findViewById(R.id.StockValuePH);
            stockPercentEarning = itemView.findViewById(R.id.StockEarningPCPH);
            stockEarning = itemView.findViewById(R.id.StockEarningPH);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.stockEarning.setText("Earning" + String.valueOf(position));
        holder.stockNameView.setText("StockName" + String.valueOf(position));
        holder.stockValueView.setText("Value" + String.valueOf(position));
        holder.stockPercentEarning.setText("Percent" + String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return 15;
    }

}
