package no.stockwallet.Adapters;

import static java.lang.Math.round;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import no.stockwallet.Handlers.StockDataRetriever;
import no.stockwallet.Model.Investment;
import no.stockwallet.R;
import yahoofinance.Stock;

public class StockRecyclerAdapter extends RecyclerView.Adapter<StockRecyclerAdapter.ViewHolder>{
    ArrayList<Investment> data;
    private int previousExpandednPosition = -1;
    private int mExpandedPosition = -1;

    DecimalFormat df = new DecimalFormat("#.##");

    public StockRecyclerAdapter(HashMap<String, Investment> data) {
        this.data = new ArrayList<>(data.values());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView stockNameView, stockValueView, stockPercentEarning, stockEarning;
        private Group detailGroup;
        private TextView volumeView, averageBuyView, intradayView, currentPriceView, tickerView;
        private Button detailViewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stockNameView = itemView.findViewById(R.id.StockNamePH);
            stockValueView = itemView.findViewById(R.id.StockValuePH);
            stockPercentEarning = itemView.findViewById(R.id.StockEarningPCPH);
            stockEarning = itemView.findViewById(R.id.StockEarningPH);
            detailGroup = itemView.findViewById(R.id.detailGroup);

            volumeView = itemView.findViewById(R.id.StockVolumePH);
            averageBuyView = itemView.findViewById(R.id.StockAveragePH);
            intradayView = itemView.findViewById(R.id.StockIntradayPH);
            currentPriceView = itemView.findViewById(R.id.StockCurrentPricePH);
            tickerView = itemView.findViewById(R.id.StockTickerPH);
            detailViewButton = itemView.findViewById(R.id.SeeStockButton);
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
        setUpDetailedView(holder, position);
        fillWithData(holder, position);

        Investment investment = data.get(position);
        String ticker = investment.getTicker();
        setUpClickListeners(holder, ticker);
    }

    private void setUpClickListeners(ViewHolder holder, String stockTicker) {
        HashMap<String, Stock> stock = new HashMap<>();
        StockDataRetriever.getInstance().getStockObject(stock, stockTicker);
        holder.detailViewButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("hashmap", stock);

            Navigation.findNavController(view).navigate(R.id.detailStockFragmentsWrapper, bundle);
        });
    }

    private void setUpDetailedView(ViewHolder holder, int position) {
        final boolean isExpanded = position == mExpandedPosition;
        holder.detailGroup.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);

        if(isExpanded)
            previousExpandednPosition = position;

        holder.itemView.setOnClickListener(view -> {
            mExpandedPosition = isExpanded ? -1 : position;
            notifyItemChanged(previousExpandednPosition);
            notifyItemChanged(position);
        });
    }

    private void fillWithData(ViewHolder holder, int position) {
        Investment stockObject = data.get(position);

        String stockName = stockObject.getFullName();
        double earning = Double.parseDouble(df.format(stockObject.getEarningsNOK()));
        double earningsPrcent = Double.parseDouble(df.format(stockObject.getEarningsPercent()));
        int markedValue = (int) Double.parseDouble(df.format(stockObject.getMarkedValueNOK()));

        holder.stockEarning.setText(String.valueOf(earning + " NOK"));
        holder.stockNameView.setText(stockName);
        holder.stockValueView.setText(String.valueOf(markedValue + " NOK"));
        holder.stockPercentEarning.setText(String.valueOf(earningsPrcent + "%"));

        String ticker = stockObject.getTicker();
        int volume = stockObject.getVolume();
        double averageBuy = Double.parseDouble(df.format(stockObject.getAvgBuyPrice()));
        double intraDay = Double.parseDouble(df.format(stockObject.getIntradayChange()));
        double currentPrice = Double.parseDouble(df.format(stockObject.getCurrentStockPrice()));
        String currency = stockObject.getCurrency();

        holder.tickerView.setText(ticker);
        holder.volumeView.setText(String.valueOf(volume));
        holder.averageBuyView.setText(String.valueOf(averageBuy));
        holder.intradayView.setText(String.valueOf(intraDay + " %"));
        holder.currentPriceView.setText(String.valueOf(currentPrice + " " + currency));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
