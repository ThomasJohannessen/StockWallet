package no.stockwallet.Adapters;

import static java.lang.Math.round;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import no.stockwallet.Model.Investment;
import no.stockwallet.R;

public class StockRecyclerAdapter extends RecyclerView.Adapter<StockRecyclerAdapter.ViewHolder>{
    ArrayList<Investment> data;
    DecimalFormat df = new DecimalFormat("#.##");


    public StockRecyclerAdapter(HashMap<String, Investment> data) {
        this.data = new ArrayList<>(data.values());
    }

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
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String stockName = data.get(position).getFullName();
        double earning = Double.parseDouble(df.format(data.get(position).getEarningsNOK()));
        double earningsPrcent = Double.parseDouble(df.format(data.get(position).getEarningsPercent()));
        int markedValue = (int) Double.parseDouble(df.format(data.get(position).getMarkedValue()));

        holder.stockEarning.setText(String.valueOf(earning + " NOK"));
        holder.stockNameView.setText(stockName);
        holder.stockValueView.setText(String.valueOf(markedValue + " NOK"));
        holder.stockPercentEarning.setText(String.valueOf(earningsPrcent + " %"));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
