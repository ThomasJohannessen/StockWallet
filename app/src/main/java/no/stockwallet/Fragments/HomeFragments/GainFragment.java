package no.stockwallet.Fragments.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import no.stockwallet.API_InvestmentDataHandler;
import no.stockwallet.Fragments.Wrappers.HomeFragmentsWrapper;
import no.stockwallet.Investment;
import no.stockwallet.R;
import no.stockwallet.StockCalculations;
import no.stockwallet.StockViewModel;

public class GainFragment extends Fragment {
    private StockViewModel viewModel;

    public GainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(StockViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gain, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView stockName1, stockName2, stockName3, stockName4, stockName5, stockName6,
                stockPrcnt1, stockPrcnt2, stockPrcnt3, stockPrcnt4, stockPrcnt5, stockPrcnt6,
                stockPrice1, stockPrice2, stockPrice3, stockPrice4, stockPrice5, stockPrice6,
                errMsg1,errMsg2;

        API_InvestmentDataHandler APIhandler = new API_InvestmentDataHandler(viewModel);
        DecimalFormat df = new DecimalFormat("#.##");
        Boolean underSixInvestments = false;

        super.onViewCreated(view, savedInstanceState);


        //Is only used to get the currency. No heavy data handling
        HomeFragmentsWrapper parent = (HomeFragmentsWrapper) getParentFragment();
        HashMap<String, Investment> investments = parent.getViewModel().getStockMap().getValue();

        if ((StockCalculations.getInstance().getBottomThreeLoserStocks().size() == 0) ||
                (StockCalculations.getInstance().getTopThreeGainerStocks().size() == 0)){
            APIhandler.findTopp3AndBootom3();

        }

        while (StockCalculations.getInstance().getTopThreeGainerStocks().size() == 0){
            try {TimeUnit.MILLISECONDS.sleep(10);}
            catch (InterruptedException e) {e.printStackTrace();}
        }
        while (StockCalculations.getInstance().getBottomThreeLoserStocks().size() == 0){
            try {TimeUnit.MILLISECONDS.sleep(10);}
            catch (InterruptedException e) {e.printStackTrace();}
        }

        ArrayList<Pair<String, BigDecimal>> topp3Arr = StockCalculations.getInstance().getTopThreeGainerStocks();
        ArrayList<Pair<String, BigDecimal>> bottom3Arr = StockCalculations.getInstance().getBottomThreeLoserStocks();

        String[] tickers = new String[6];

        for(int i=0; i<6; i++){

            if (i<3) {
                if (bottom3Arr.get(i).first == null)
                    underSixInvestments = true;
            }
            else if(i>3){
                if (topp3Arr.get(i-3).first == null)
                    underSixInvestments = true;
            }
            if (i<3) {
                tickers[i] = bottom3Arr.get(i).first;
            }else{
                tickers[i] = topp3Arr.get(i-3).first;
            }
        }

        stockPrcnt1 = view.findViewById(R.id.stockPCR1);
        stockName1 = view.findViewById(R.id.stockNameR1);
        stockPrice1 = view.findViewById(R.id.stockPriceR1);
        stockPrcnt2 = view.findViewById(R.id.stockPCR2);
        stockName2 = view.findViewById(R.id.stockNameR2);
        stockPrice2 = view.findViewById(R.id.stockPriceR2);
        stockPrcnt3 = view.findViewById(R.id.stockPCR3);
        stockName3 = view.findViewById(R.id.stockNameR3);
        stockPrice3 = view.findViewById(R.id.stockPriceR3);
        stockPrcnt4 = view.findViewById(R.id.stockPCR4);
        stockName4 = view.findViewById(R.id.stockNameR4);
        stockPrice4 = view.findViewById(R.id.stockPriceR4);
        stockPrcnt5 = view.findViewById(R.id.stockPCR5);
        stockName5 = view.findViewById(R.id.stockNameR5);
        stockPrice5 = view.findViewById(R.id.stockPriceR5);
        stockPrcnt6 = view.findViewById(R.id.stockPCR6);
        stockName6 = view.findViewById(R.id.stockNameR6);
        stockPrice6 = view.findViewById(R.id.stockPriceR6);


        if (underSixInvestments){
            stockPrcnt1.setText("");
            stockName1.setText("");
            stockPrice1.setText("");
            stockPrcnt2.setText("");
            stockName2.setText("");
            stockPrice2.setText("");
            stockPrcnt3.setText("");
            stockName3.setText("");
            stockPrice3.setText("");
            stockPrcnt4.setText("");
            stockName4.setText("");
            stockPrice4.setText("");
            stockPrcnt5.setText("");
            stockName5.setText("");
            stockPrice5.setText("");
            stockPrcnt6.setText("");
            stockName6.setText("");
            stockPrice6.setText("");

            errMsg1 = view.findViewById(R.id.ErrorMsgTooFewStocks);
            errMsg2 = view.findViewById(R.id.ErrorMsgTooFewStocks2);
            errMsg1.setText("Du må ha 6 aksjer i din portefølge for å bruke denne funksjonen.");
            errMsg2.setText("Du må ha 6 aksjer i din portefølge for å bruke denne funksjonen.");

        }
        else {

            if (APIhandler.getStockPricesTopp3Bottom3Map() == null){
                APIhandler.fetchStockPricesForToppAndBottom3(tickers);
            }
            HashMap<String, BigDecimal> stockPrices = APIhandler.getStockPricesTopp3Bottom3Map();

            while (stockPrices.size() < tickers.length) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            stockPrcnt1.setText(String.valueOf(topp3Arr.get(0).second + " %"));
            stockName1.setText(String.valueOf(topp3Arr.get(0).first));
            stockPrice1.setText(String.valueOf(Double.parseDouble(df.format(stockPrices.get(topp3Arr.get(0).first))) + " " + investments.get(topp3Arr.get(0).first).getCurrency()));

            stockPrcnt2.setText(String.valueOf(topp3Arr.get(1).second + " %"));
            stockName2.setText(String.valueOf(topp3Arr.get(1).first));
            stockPrice2.setText(String.valueOf(Double.parseDouble(df.format(stockPrices.get(topp3Arr.get(1).first))) + " " + investments.get(topp3Arr.get(1).first).getCurrency()));

            stockPrcnt3.setText(String.valueOf(topp3Arr.get(2).second + " %"));
            stockName3.setText(String.valueOf(topp3Arr.get(2).first));
            stockPrice3.setText(String.valueOf(Double.parseDouble(df.format(stockPrices.get(topp3Arr.get(2).first))) + " " + investments.get(topp3Arr.get(2).first).getCurrency()));

            stockPrcnt4.setText(String.valueOf(bottom3Arr.get(0).second + " %"));
            stockName4.setText(String.valueOf(bottom3Arr.get(0).first));
            stockPrice4.setText(String.valueOf(Double.parseDouble(df.format(stockPrices.get(bottom3Arr.get(0).first))) + " " + investments.get(bottom3Arr.get(0).first).getCurrency()));

            stockPrcnt5.setText(String.valueOf(bottom3Arr.get(1).second + " %"));
            stockName5.setText(String.valueOf(bottom3Arr.get(1).first));
            stockPrice5.setText(String.valueOf(Double.parseDouble(df.format(stockPrices.get(bottom3Arr.get(1).first))) + " " + investments.get(bottom3Arr.get(1).first).getCurrency()));

            stockPrcnt6.setText(String.valueOf(bottom3Arr.get(2).second + " %"));
            stockName6.setText(String.valueOf(bottom3Arr.get(2).first));
            stockPrice6.setText(String.valueOf(Double.parseDouble(df.format(stockPrices.get(bottom3Arr.get(2).first))) + " " + investments.get(bottom3Arr.get(2).first).getCurrency()));
        }
    }
}