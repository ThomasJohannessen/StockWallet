package no.stockwallet.Adapters;

import static java.lang.Math.round;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import no.stockwallet.Investment;
import no.stockwallet.R;
import no.stockwallet.StockCalculations;
import no.stockwallet.StockDataRetriever;
import no.stockwallet.StockViewModel;
import yahoofinance.Stock;

public class StockRecyclerAdapter extends RecyclerView.Adapter<StockRecyclerAdapter.ViewHolder>{
    ArrayList<Investment> data;
    long fetchedTime = 0;
    HashMap<String, Investment> HashMapData;
    HashMap<String, String> stockNames = new HashMap<>();
    HashMap<String, Double> earnings = new HashMap<>();
    HashMap<String, Double> earningsPercent = new HashMap<>();
    HashMap<String, Double> markedValues = new HashMap<>();

    DecimalFormat df = new DecimalFormat("#.##");


    public StockRecyclerAdapter(HashMap<String, Investment> data) {
        this.HashMapData = data;
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

        String stockName;
        double earning, earningsPrcent;
        int markedValue;

        //get stockname logic
        if (!stockNames.containsKey(data.get(position).getTicker())){
            HashMap<String, Stock> temp = new HashMap<>();
            StockDataRetriever.getInstance().getStockObject(temp,data.get(position).getTicker());
            while (temp.size() < 1){
                try {TimeUnit.MILLISECONDS.sleep(10);}
                catch (InterruptedException e) {e.printStackTrace();}
            }
            stockName = temp.get("Stock").getName();
            stockNames.put(data.get(position).getTicker(),temp.get("Stock").getName());
        }
        else{
            stockName = stockNames.get(data.get(position).getTicker());
        }

        if (System.currentTimeMillis() > (fetchedTime + 120000)){
            Log.d("TotaltInvestert-earningsNOK", String.valueOf(earnings.size()));
            Log.d("TotaltInvestert-names", String.valueOf(stockNames.size()));
            Log.d("TotaltInvestert-earnings%", String.valueOf(earningsPercent.size()));
            Log.d("TotaltInvestert-value", String.valueOf(markedValues.size()));

            fetchedTime = System.currentTimeMillis();
            earnings = StockCalculations.getInstance().getEarningsNOKMultipleStocks(HashMapData);
            earningsPercent = StockCalculations.getInstance().getEarningsPercentMultipleStocks(HashMapData);
            markedValues = StockCalculations.getInstance().getMarkedValueNOKMultipleStocks(HashMapData);

        }

        earning = Double.parseDouble(df.format(earnings.get(data.get(position).getTicker())));
        earningsPrcent = Double.parseDouble(df.format(earningsPercent.get(data.get(position).getTicker())));
        markedValue = (int) Double.parseDouble(df.format(markedValues.get(data.get(position).getTicker())));

        if (data.get(position).getTicker() != holder.stockNameView.getText()){

            holder.stockEarning.setText(String.valueOf(earning + " NOK"));
            holder.stockNameView.setText(stockName);
            holder.stockValueView.setText(String.valueOf(markedValue + " NOK"));
            holder.stockPercentEarning.setText(String.valueOf(earningsPrcent + " %"));
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
