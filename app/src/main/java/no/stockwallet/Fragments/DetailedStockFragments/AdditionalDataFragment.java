package no.stockwallet.Fragments.DetailedStockFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

import no.stockwallet.R;
import yahoofinance.Stock;

public class AdditionalDataFragment extends Fragment {

    public AdditionalDataFragment() {
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
        return inflater.inflate(R.layout.fragment_additional_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillWithData(view);
        setUpButtons(view);
    }

    private void setUpButtons(View mainView) {
        mainView.findViewById(R.id.buyButton).setOnClickListener(view -> {
            Navigation.findNavController(mainView).navigate(R.id.buySellFragmentsWrapper);
        });
    }

    private void fillWithData(View view) {
        TextView tickerView = (TextView) view.findViewById(R.id.SingleStockTickerPH);
        TextView dailyChangeView = (TextView) view.findViewById(R.id.SingleStockChangePH);
        TextView costView = (TextView) view.findViewById(R.id.SingleStockCostPH);
        TextView marketValueView = (TextView) view.findViewById(R.id.SingleStockMarketPH);
        TextView BVPSView = (TextView) view.findViewById(R.id.SingleStockBVSPH);
        TextView PEView = (TextView) view.findViewById(R.id.SingleStockPEPH);
        TextView yearLow = (TextView) view.findViewById(R.id.SingleStockYearBottomPH);
        TextView yearHigh = (TextView) view.findViewById(R.id.SingleStockYearTopPH);

        try {
            Bundle parentArguments = getParentFragment().getArguments();
            HashMap<String, Stock> map = (HashMap<String, Stock>) parentArguments.getSerializable("hashmap");
            Stock stock = map.get("Stock");

            tickerView.setText(stock.getSymbol() == null ? "N/A" : stock.getSymbol());
            dailyChangeView.setText(stock.getQuote().getChange() == null ? "N/A" : String.valueOf(Math.round((stock.getQuote().getChangeInPercent().doubleValue())*100.0)/100.0 + " %"));
            costView.setText(stock.getQuote().getPrice() == null ? "N/A" : String.valueOf(stock.getQuote().getPrice()+ " " + stock.getCurrency()));
            marketValueView.setText(stock.getStats().getMarketCap() == null ? "N/A" : String.valueOf(stock.getStats().getMarketCap().unscaledValue()+ " " + stock.getCurrency()));
            BVPSView.setText(stock.getStats().getBookValuePerShare() == null ? "N/A" : String.valueOf(stock.getStats().getBookValuePerShare()));
            PEView.setText(stock.getStats().getPe() == null ? "N/A" : String.valueOf(stock.getStats().getPe()));
            yearLow.setText(stock.getQuote().getChangeFromYearLowInPercent() == null ? "N/A" : String.valueOf(stock.getQuote().getChangeFromYearLowInPercent()));
            yearHigh.setText(stock.getQuote().getChangeFromYearHighInPercent() == null ? "N/A" : String.valueOf(stock.getQuote().getChangeFromYearHighInPercent()));
        }
        catch (NullPointerException e) {
            Snackbar.make(view, "An error occured while communicating with server. Some data might be missing", BaseTransientBottomBar.LENGTH_LONG).show();
            tickerView.setText("N/A");
            dailyChangeView.setText("N/A");
            costView.setText("N/A");
            marketValueView.setText("N/A");
            BVPSView.setText("N/A");
            PEView.setText("N/A");
            yearLow.setText("N/A");
            yearHigh.setText("N/A");
        }

    }
}